package me.nickac.survivalplus.energy;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.ITickableBlock;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class EnergyMap {

    public class EnergyTicker implements Runnable {
        @Override
        public void run() {
            for (EnergyCircuit circuit : getCircuits()) {
                for (CustomBlock block : circuit.getBlocks()) {

                    if (block instanceof ITickableBlock)
                        ((ITickableBlock) block).tick();

                    if (block instanceof IEnergyReceiver) {
                        final IEnergyReceiver receiver = (IEnergyReceiver) block;
                        circuit.powerReceiver(receiver);
                    }
                }
            }
        }
    }


    @Inject
    public EnergyMap(PluginContainer container) {
        registerBlockTick(container);
    }

    private void registerBlockTick(PluginContainer container) {
        Sponge.getScheduler().createTaskBuilder()
                .interval(50, TimeUnit.MILLISECONDS)
                .execute(new EnergyTicker())
                .submit(container);
    }

    private List<EnergyCircuit> circuits = new ArrayList<>();
    private List<CustomBlock> queuedBlocks = new ArrayList<>();

    public List<EnergyCircuit> getCircuits() {
        return circuits;
    }

    public void queueBlock(CustomBlock block) {
        queuedBlocks.add(block);
    }

    public void processQueuedBlocks() {
        queuedBlocks.forEach(this::recursiveCreateNewCircuit);
        queuedBlocks.clear();
    }

    public void recursiveCreateNewCircuit(CustomBlock block) {
        final List<EnergyCircuit> sideCircuits =
                Arrays.stream(CustomBlock.getEnergySideDirections()).map(d -> block.getLocation().getRelative(d))
                .map(this::getCircuitOfBlock)
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        Optional<EnergyCircuit> circuitOpt = sideCircuits.stream().findFirst();

        //Remove from all remaining circuits on it's sides
        final Stream<CustomBlock> blocksToMerge = sideCircuits.stream().skip(1).filter(c -> removeFromCircuit(block,
                c)).filter(c -> circuits.remove(c)).flatMap(c -> c.getBlocks().stream());

        if (!isBlockOnAnyCircuit(block)) {
            final EnergyCircuit circuit = circuitOpt.orElse(new EnergyCircuit());
            blocksToMerge.forEach(circuit::recursiveAddBlock);
            circuit.recursiveAddBlock(block);
            if (!this.circuits.contains(circuit))
                this.circuits.add(circuit);
        }
    }

    public void removeFromAnyCircuit(CustomBlock block) {
        final List<EnergyCircuit> toRegen =
                circuits.stream()
                        .filter(c -> c.getBlocks().removeIf(cc -> cc.equals(block)))
                        .filter(c -> !c.getBlocks().isEmpty())
                        .collect(Collectors.toList());
        circuits.removeIf(c -> c.getBlocks().isEmpty());

        toRegen.forEach(c -> circuits.remove(c));

        toRegen.stream().flatMap(c -> c.getBlocks().stream()).forEach(this::recursiveCreateNewCircuit);

    }
    private boolean removeFromCircuit(CustomBlock block, EnergyCircuit circuit) {
        circuit.getBlocks().remove(block);
        circuits.removeIf(c -> c.equals(circuit) && c.getBlocks().isEmpty());
        return true;
    }

    private boolean isBlockOnAnyCircuit(CustomBlock block) {
        return circuits.stream().anyMatch(c -> c.isOnCircuit(block.getLocation()));
    }

    public boolean isBlockOnAnyCircuit(Location loc) {
        return circuits.stream().anyMatch(c -> c.isOnCircuit(loc));
    }

    public Optional<EnergyCircuit> getCircuitOfBlock(CustomBlock block) {
        return circuits.stream().filter(c -> c.isOnCircuit(block.getLocation())).findFirst();
    }

    public Optional<EnergyCircuit> getCircuitOfBlock(Location loc) {
        return circuits.stream().filter(c -> c.isOnCircuit(loc)).findFirst();
    }

}
