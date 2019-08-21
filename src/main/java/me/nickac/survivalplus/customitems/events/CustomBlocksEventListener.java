package me.nickac.survivalplus.customitems.events;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.weighted.WeightedSerializableObject;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class CustomBlocksEventListener {

    @Inject
    private CustomItemManager itemManager;

    @Listener
    public void onInteractBlock(InteractBlockEvent.Secondary event, @First Player p) {
        Optional<ItemStackSnapshot> item = event.getContext().get(EventContextKeys.USED_ITEM);

        if (!item.isPresent()) return;

        ItemStackSnapshot snapshot = item.get();

        if (itemManager.isManagedItem(snapshot)) {

            CustomItemInformation info = itemManager.getCustomItemDataFromItem(snapshot);

            Location<World> loc =
                    event.getTargetBlock().getLocation().orElse(p.getLocation()).getBlockRelative(event.getTargetSide());

            event.setCancelled(true);

            if (!loc.getExtent().getIntersectingEntities(new AABB(loc.getBlockPosition(),
                    loc.getBlockPosition().add(1, 1, 1))).isEmpty()) return;

            loc.setBlock(BlockState.builder()
                    .blockType(BlockTypes.MOB_SPAWNER)
                    .build());

//            loc.getTileEntity().get().offer(loc.getTileEntity().get().getOrCreate(CustomItemInformationData.class).get());


            DataTransactionResult result = loc.offer(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE, info);
            loc.offer(Keys.SPAWNER_REQUIRED_PLAYER_RANGE, (short) 0);
            loc.offer(Keys.SPAWNER_MAXIMUM_NEARBY_ENTITIES, (short) 0);
            loc.offer(Keys.SPAWNER_MAXIMUM_DELAY, (short) 0);
            loc.offer(Keys.SPAWNER_MINIMUM_DELAY, (short) 0);
            loc.offer(Keys.SPAWNER_SPAWN_RANGE, (short) 0);

            ArmorStand armorStand = (ArmorStand) loc.getExtent().createEntity(EntityTypes.ARMOR_STAND,
                    loc.getBlockPosition());
            armorStand.setRotation(Vector3d.ZERO);
            armorStand.offer(armorStand.basePlate().set(false));
            armorStand.offer(armorStand.marker().set(true));
            armorStand.setHeadRotation(new Vector3d(0, 0, 0));
            armorStand.offer(Keys.INVISIBLE, true);
            armorStand.setHelmet(itemManager.generateCustomItemStack(info.getOrdinal()));

            WeightedSerializableObject<EntityArchetype> entity =
                    new WeightedSerializableObject<>(EntityArchetype.builder()
                            .from(armorStand)
                            .build(), 100);
            loc.offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, entity);
            armorStand.remove();
        }
    }

/*
    @Listener
    public void onChangeBlockBreak(ChangeBlockEvent.Break event, @First Player pl) {
        BlockSnapshot block = event.getContext().get(EventContextKeys.BLOCK_HIT).orElse(null);
        if (block == null || !block.getLocation().isPresent()) return;

        ManagedTypeData.Immutable data =
                event.getTransactions().stream()
                        .map(c -> c.getOriginal().get(ManagedTypeData.Immutable.class).orElse(null))
                        .filter(Objects::nonNull)
                        .findFirst().orElse(null);
        if (data != null) {
            pl.spawnParticles(ParticleEffect.builder()
                    .type(ParticleTypes.BREAK_BLOCK)
                    .option(ParticleOptions.BLOCK_STATE,
                            BlockState.builder().blockType(BlockTypes.STONE).build())
                    .build(), block.getPosition().toDouble());
        }

    }
*/

    @Listener
    public void onInteractItem(InteractItemEvent event, @First Player p) {
        if (itemManager.isManagedItem(event.getItemStack())) {
            event.setCancelled(true);
        }
    }
}