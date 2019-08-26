package me.nickac.survivalplus.customitems.internal;

import cofh.redstoneflux.api.IEnergyConnection;
import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.energy.EnergyMap;
import me.nickac.survivalplus.managers.CustomItemManager;
import me.nickac.survivalplus.misc.MiscUtils;
import me.nickac.survivalplus.misc.data.CustomKeys;
import me.nickac.survivalplus.misc.data.impl.CustomItemData;
import me.nickac.survivalplus.misc.data.impl.CustomItemInfoData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.MobSpawner;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.weighted.WeightedSerializableObject;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class CustomBlock extends CustomItem {
    protected static final Direction[] ENERGY_SIDE_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH,
            Direction.WEST,
            Direction.EAST, Direction.UP, Direction.DOWN};
    private static final DataQuery LOC_X = DataQuery.of("LocX");
    private static final DataQuery LOC_Y = DataQuery.of("LocY");
    private static final DataQuery LOC_Z = DataQuery.of("LocZ");
    private static final DataQuery LOC_EXT = DataQuery.of("LocExt");
    private Location<World> location;
    private boolean markedForRemoval;

    @Inject
    private static EnergyMap energyMap;

    public static Direction[] getEnergySideDirections() {
        return ENERGY_SIDE_DIRECTIONS;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void forRemoval() {
        markedForRemoval = true;
    }

    public Location<World> getLocation() {
        return location;
    }

    @Override
    public void loadInfo(DataView view) {
        if (location == null && view.contains(LOC_X)) {
            final World world = Sponge.getServer().getWorld(UUID.fromString(view.getString(LOC_EXT).get())).get();
            final Integer x = view.getInt(LOC_X).get();
            final Integer y = view.getInt(LOC_Y).get();
            final Integer z = view.getInt(LOC_Z).get();
            location = new Location<>(world, x, y, z);
        }
    }

    protected CustomItemManager getItemManager() {
        return itemManager;
    }

    public void onPrePlace(Player player, Consumer<CustomItemInformation> modifyInfo) {
    }

    private void onPlace(Player player) {

    }

    public void onBlockInteract(Player player, InteractBlockEvent event) {
    }

    @Override
    public final DataContainer toContainer() {
        final DataContainer view = getInfo().toContainer();
        final DataContainer info = DataContainer.createNew();

        saveInfo(info);
        if (location != null) {
            info.set(LOC_X, location.getBlockX());
            info.set(LOC_Y, location.getBlockY());
            info.set(LOC_Z, location.getBlockZ());
            info.set(LOC_EXT, location.getExtent().getUniqueId().toString());
        }
        view.set(DataQuery.of("ItemInfo"), info);

        return view;
    }

    @Override
    public void setInfo(CustomItemInformation info) {
        if (info == null) return;
        super.setInfo(info);

        if (location != null) {
            location.getTileEntity().ifPresent(ent -> {
                if (ent instanceof MobSpawner) {
                    final EntityArchetype entity =
                            ((MobSpawner) ent).getMobSpawnerData().nextEntityToSpawn().get().get();

                    ArmorStand newAs = getItemArmorStand(ent.getLocation(), info.getOrdinal());
                    WeightedSerializableObject<EntityArchetype> entityArch =
                            new WeightedSerializableObject<>(EntityArchetype.builder()
                                    .from(newAs)
                                    .build(), 100);
                    ent.offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, entityArch);
                    newAs.remove();
                }
            });
        }

    }

    public final void placeBlock(Location<World> loc, InteractBlockEvent.Secondary event, Player p) {
        event.setCancelled(true);

        if (!loc.getExtent().getIntersectingEntities(new AABB(loc.getBlockPosition(),
                loc.getBlockPosition().add(1, 1, 1))).isEmpty()) return;

        location = loc;

        loc.setBlock(BlockState.builder()
                .blockType(BlockTypes.MOB_SPAWNER)
                .build());

        if (!loc.getExtent().getIntersectingEntities(new AABB(loc.getBlockPosition(),
                loc.getBlockPosition().add(1, 1, 1))).isEmpty()) return;

        loc.setBlock(BlockState.builder()
                .blockType(BlockTypes.MOB_SPAWNER)
                .build());

        TileEntity te = loc.getTileEntity().get();
        te.offer(te.getOrCreate(CustomItemData.class).get());
        te.offer(te.getOrCreate(CustomItemInfoData.class).get());

        loc.tryOffer(CustomKeys.CUSTOM_ITEM_VALUE, this); //It's not serialized

        loc.offer(Keys.SPAWNER_REQUIRED_PLAYER_RANGE, (short) 0);
        loc.offer(Keys.SPAWNER_MAXIMUM_NEARBY_ENTITIES, (short) 0);
        loc.offer(Keys.SPAWNER_MAXIMUM_DELAY, (short) 0);
        loc.offer(Keys.SPAWNER_MINIMUM_DELAY, (short) 0);
        loc.offer(Keys.SPAWNER_REMAINING_DELAY, (short) 0);
        loc.offer(Keys.SPAWNER_SPAWN_RANGE, (short) 0);

        final CustomItemInformation[] infoToUse = {getInfo().isDirectional() ?
                getInfo().getDirectionalInfo(MiscUtils.yawToFace((float) p.getHeadRotation().getY()))
                : getInfo()};
        onPrePlace(p, i -> infoToUse[0] = i);
        ArmorStand armorStand = getItemArmorStand(loc, infoToUse[0].getOrdinal());

        WeightedSerializableObject<EntityArchetype> entity =
                new WeightedSerializableObject<>(EntityArchetype.builder()
                        .from(armorStand)
                        .build(), 100);
        loc.offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, entity);
        armorStand.remove();

        onPlace(p);
        updateBlocksAround();

        if (IEnergyConnection.class.isAssignableFrom(getClass())) {
            energyMap.recursiveCreateNewCircuit(this);
        }
    }

    private void updateBlocksAround() {
        for (Direction dir : ENERGY_SIDE_DIRECTIONS) {
            final Location relative = getLocation().getBlockRelative(dir.getOpposite());
            final BlockSnapshot block = relative.createSnapshot();
            if (getItemManager().isManagedBlock(block)) {
                final CustomBlock info = getItemManager().getManagedBlockInfo(block);
                if (info instanceof IEnergyConnection) {
                    ((IEnergyConnection) info).updateConnection();
                }
            }
        }
    }

    private ArmorStand getItemArmorStand(Location<World> loc, int ordinal) {
        ArmorStand armorStand = (ArmorStand) loc.getExtent().createEntity(EntityTypes.ARMOR_STAND,
                loc.getBlockPosition());
        armorStand.setHeadRotation(Vector3d.ZERO);
        armorStand.setRotation(Vector3d.ZERO);
        armorStand.offer(armorStand.basePlate().set(false));
        armorStand.offer(armorStand.marker().set(true));
        armorStand.setHeadRotation(new Vector3d(0, 0, 0));
        armorStand.offer(Keys.INVISIBLE, true);

        armorStand.setHelmet(itemManager.generateCustomItemStack(ordinal));
        return armorStand;
    }

    private void onBreak(ChangeBlockEvent.Break event, Player p) {

    }

    public final void handleBreak(ChangeBlockEvent.Break event, Player p) {
        onBreak(event, p);
        updateBlocksAround();

        if (IEnergyConnection.class.isAssignableFrom(getClass())) {
            energyMap.removeFromAnyCircuit(this);
        }
    }
}
