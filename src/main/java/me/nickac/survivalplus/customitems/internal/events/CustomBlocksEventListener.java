package me.nickac.survivalplus.customitems.internal.events;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.weighted.WeightedTable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.Random;

public class CustomBlocksEventListener {

    @Inject
    private CustomItemManager itemManager;

    private WeightedTable<BlockType> blocks;
    private Random random = new Random();

    public CustomBlocksEventListener() {
        blocks = new WeightedTable<>();
        blocks.add(BlockTypes.COBBLESTONE, 7);
        blocks.add(BlockTypes.COAL_ORE, 4);
        blocks.add(BlockTypes.IRON_ORE, 3);
        blocks.add(BlockTypes.GOLD_ORE, 2);
        blocks.add(BlockTypes.DIAMOND_ORE, 1);
        blocks.add(BlockTypes.EMERALD_ORE, 0.5);
    }

/*
    @Listener
    public void EVERYTHING(Event e, @Root Player player) {
        System.out.println(e.getClass().getName());
    }
*/

    @Listener
    public void onChangeBlock(ChangeBlockEvent.Modify event) {
        if (event.getContext().containsKey(EventContextKeys.LIQUID_MIX)) {
            final Transaction<BlockSnapshot> transaction = event.getTransactions().get(0);

            final BlockType result = blocks.get(random).get(0);

            if (result != BlockTypes.COBBLESTONE) {
                final Location<World> loc = transaction.getFinal().getLocation().get();
                transaction.setValid(false);

                loc.setBlock(BlockState.builder().blockType(result).build());
                loc.getExtent().playSound(SoundTypes.BLOCK_LAVA_EXTINGUISH, loc.getPosition(), 0.5);
            }
        }
    }

    @Listener
    public void onCustomBlockInteract(InteractBlockEvent event, @Root Player p) {
        Optional<BlockSnapshot> snapshot = event.getContext().get(EventContextKeys.BLOCK_HIT);
        if (snapshot.isPresent()) {
            BlockSnapshot blockSnapshot = snapshot.get();
            if (itemManager.isManagedBlock(blockSnapshot)) {
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
        final BlockSnapshot original = event.getTransactions().get(0).getOriginal();

        if (itemManager.isManagedBlock(original)) {
            event.filterAll();
            event.setCancelled(true);
            original.restore(true, BlockChangeFlags.NONE);
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
