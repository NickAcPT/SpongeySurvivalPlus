package me.nickac.survivalplus.customitems.internal.events;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class CustomBlocksEventListener {

    @Inject
    private CustomItemManager itemManager;

    @Listener
    public void onCustomBlockInteract(InteractBlockEvent event, @Root Player p) {
        Optional<BlockSnapshot> snapshot = event.getContext().get(EventContextKeys.BLOCK_HIT);
        if (snapshot.isPresent()) {
            BlockSnapshot blockSnapshot = snapshot.get();

            if (itemManager.isManagedBlock(blockSnapshot)) {
                if (event instanceof InteractBlockEvent.Primary)
                itemManager.getManagedBlockInfo(blockSnapshot).onBlockInteract(p, event);
            }

        }
    }

    @Listener
    public void onCustomBlockPlace(InteractBlockEvent.Secondary event, @Root Player p) {
        Optional<ItemStackSnapshot> item = event.getContext().get(EventContextKeys.USED_ITEM);

        if (!item.isPresent()) return;

        ItemStackSnapshot snapshot = item.get();

        if (itemManager.isManagedBlockItem(snapshot)) {

            CustomItemInformation info = itemManager.getCustomItemDataFromItem(snapshot);

            Location<World> loc =
                    event.getTargetBlock().getLocation().orElse(p.getLocation()).getBlockRelative(event.getTargetSide());

            CustomBlock block = (CustomBlock) info.createNewInstance();
            block.setInfo(info);
            block.placeBlock(loc, event, p);

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
