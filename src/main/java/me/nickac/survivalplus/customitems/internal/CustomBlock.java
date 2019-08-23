package me.nickac.survivalplus.customitems.internal;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import me.nickac.survivalplus.MiscUtils;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.data.impl.CustomItemData;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.weighted.WeightedSerializableObject;
import org.spongepowered.api.world.Location;

public abstract class CustomBlock extends CustomItem {

    @Inject
    private static CustomItemManager itemManager;

    public void onPlace(Player player) {
    }

    public void onBlockInteract(Player player, InteractBlockEvent event) {
    }

    @Override
    public final DataContainer toContainer() {
        final DataContainer view = getInfo().toContainer();
        final DataContainer info = DataContainer.createNew();

        saveInfo(info);
        view.set(DataQuery.of("ItemInfo"), info);

        return view;
    }

    public final void placeBlock(Location loc, InteractBlockEvent.Secondary event, Player p) {
        event.setCancelled(true);

        if (!loc.getExtent().getIntersectingEntities(new AABB(loc.getBlockPosition(),
                loc.getBlockPosition().add(1, 1, 1))).isEmpty()) return;

        loc.setBlock(BlockState.builder()
                .blockType(BlockTypes.MOB_SPAWNER)
                .build());

        if (!loc.getExtent().getIntersectingEntities(new AABB(loc.getBlockPosition(),
                loc.getBlockPosition().add(1, 1, 1))).isEmpty()) return;

        loc.setBlock(BlockState.builder()
                .blockType(BlockTypes.MOB_SPAWNER)
                .build());

        TileEntity te = (TileEntity) loc.getTileEntity().get();
        te.offer(te.getOrCreate(CustomItemData.class).get());
        te.offer(te.getOrCreate(CustomItemInfoData.class).get());

        loc.tryOffer(CustomKeys.CUSTOM_ITEM_VALUE, this); //It's not serialized

        loc.offer(Keys.SPAWNER_REQUIRED_PLAYER_RANGE, (short) 0);
        loc.offer(Keys.SPAWNER_MAXIMUM_NEARBY_ENTITIES, (short) 0);
        loc.offer(Keys.SPAWNER_MAXIMUM_DELAY, (short)0);
        loc.offer(Keys.SPAWNER_MINIMUM_DELAY, (short)0);
        loc.offer(Keys.SPAWNER_REMAINING_DELAY, (short)0);
        loc.offer(Keys.SPAWNER_SPAWN_RANGE, (short) 0);

        ArmorStand armorStand = (ArmorStand) loc.getExtent().createEntity(EntityTypes.ARMOR_STAND,
                loc.getBlockPosition());
        armorStand.setHeadRotation(Vector3d.ZERO);
        armorStand.setRotation(Vector3d.ZERO);
        armorStand.offer(armorStand.basePlate().set(false));
        armorStand.offer(armorStand.marker().set(true));
        armorStand.setHeadRotation(new Vector3d(0, 0, 0));
        armorStand.offer(Keys.INVISIBLE, true);
        armorStand.setHelmet(itemManager.generateCustomItemStack(((getInfo().isDirectional() ?
                getInfo().getDirectionalInfo(MiscUtils.yawToFace((float) p.getHeadRotation().getY()))
                : getInfo())).getOrdinal()));

        WeightedSerializableObject<EntityArchetype> entity =
                new WeightedSerializableObject<>(EntityArchetype.builder()
                        .from(armorStand)
                        .build(), 100);
        loc.offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, entity);
        armorStand.remove();
    }

}
