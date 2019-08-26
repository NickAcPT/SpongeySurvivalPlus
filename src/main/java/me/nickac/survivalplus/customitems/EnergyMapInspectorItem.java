package me.nickac.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.energy.EnergyCircuit;
import me.nickac.survivalplus.energy.EnergyMap;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Optional;
import java.util.function.Predicate;

public class EnergyMapInspectorItem extends CustomItem {
    @Inject
    private static EnergyMap energyMap;
    private Modes useMode = Modes.ALL;

    @Override
    public void saveInfo(DataView view) {
        view.set(DataQuery.of("Mode"), useMode.name());
    }

    @Override
    public void loadInfo(DataView view) {
        useMode = Modes.valueOf(view.getString(DataQuery.of("Mode")).orElse("ALL"));
    }

    @Override
    public void onInteract(Player player, InteractItemEvent event) {
        final Boolean sneaking = player.get(Keys.IS_SNEAKING).orElse(false);
        if (sneaking) {
            useMode = getNextMode(useMode);
        }
        player.sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.GOLD, TextStyles.BOLD, sneaking ? "Changed " +
                        "highlight mode to: " : "Current highlight mode: ",
                TextColors.GREEN, TextStyles.UNDERLINE, useMode.getDescription()));
        event.setCancelled(true);
    }

    private Modes getNextMode(Modes useMode) {
        int currentMode = useMode.ordinal();
        currentMode += 1;
        if (currentMode > Modes.values().length - 1)
            currentMode = 0;


        return Modes.values()[currentMode];
    }

    @Override
    public void onInteract(Player player, InteractBlockEvent event) {
        final BlockSnapshot block = event.getTargetBlock();

        if (itemManager.isManagedBlock(block)) {
            final CustomBlock info = itemManager.getManagedBlockInfo(block);
            final Optional<EnergyCircuit> circuitOfBlock = energyMap.getCircuitOfBlock(info);

            if (circuitOfBlock.isPresent()) {
                final EnergyCircuit circuit = circuitOfBlock.get();

                circuit.getBlocks().stream().filter(useMode.getPredicate()).forEach(b -> {
                    final Entity entity = b.getLocation().getExtent().createEntity(EntityTypes.FALLING_BLOCK,
                            b.getLocation().getPosition().add(0.5f, 0, 0.5f));
                    if (entity instanceof FallingBlock) {
                        final FallingBlock fallingBlock = (FallingBlock) entity;
                        entity.offer(fallingBlock.blockState().set(BlockState.builder().blockType(BlockTypes.GLASS).build()));
                        entity.offer(fallingBlock.fallTime().set(500));
                        entity.offer(fallingBlock.canDropAsItem().set(false));
                        entity.offer(fallingBlock.gravity().set(false));
                        entity.offer(Keys.GLOWING, true);
                    }
                    b.getLocation().spawnEntity(entity);
                });
            } else {
                player.sendMessage(Text.of(TextColors.RED, "Block not connected to any circuits."));
            }


        }
    }

    enum Modes {
        ALL("All", block -> true),
        CABLES_ONLY("Show Wires", block -> block instanceof WireBlock),
        MACHINE_BLOCKS("Machine Blocks", block -> block instanceof IEnergyHandler && !(block instanceof WireBlock)),
        ENERGY_PROVIDERS("Energy Providers", block -> block instanceof IEnergyProvider),
        ENERGY_RECEIVERS("Energy Receivers", block -> block instanceof IEnergyReceiver)/*,
        ENERGY_CONNECTIONS("Energy Connections", block -> block instanceof IEnergyConnection)*/;

        private final String description;
        private final Predicate<? super CustomBlock> predicate;

        Modes(String description, Predicate<? super CustomBlock> predicate) {

            this.description = description;
            this.predicate = predicate;
        }

        public String getDescription() {
            return description;
        }

        public Predicate<? super CustomBlock> getPredicate() {
            return predicate;
        }
    }
}
