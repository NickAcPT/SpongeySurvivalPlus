package me.nickac.survivalplus.energy;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EnergyCircuit {
    @Inject
    private static CustomItemManager itemManager;
    private UUID circuitId = UUID.randomUUID();
    private List<CustomBlock> blocks = new ArrayList<>();

    public List<CustomBlock> getBlocks() {
        return blocks;
    }

    public UUID getCircuitId() {
        return circuitId;
    }

    public boolean isOnCircuit(Location location) {
        return blocks.stream().anyMatch(b -> b.getLocation().equals(location));
    }

    public void addBlock(CustomBlock block) {
        if (!isOnCircuit(block.getLocation())) {
            blocks.add(block);
        }
    }

    public void recursiveAddBlock(CustomBlock block) {
        if (isOnCircuit(block.getLocation())) return;

        addBlock(block);
        for (Direction dir : CustomBlock.getEnergySideDirections()) {
            final Location<World> relative = block.getLocation().getRelative(dir);
            try {
                relative.getBlock(); //Request the block to load it - just to be sure -
            } catch (Exception e) {
                e.printStackTrace();
            }
            final BlockSnapshot snapshot = relative.createSnapshot();
            if (itemManager.isManagedBlock(snapshot)) {
                final CustomBlock customBlock = itemManager.getManagedBlockInfo(snapshot);
                recursiveAddBlock(customBlock);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnergyCircuit that = (EnergyCircuit) o;
        return circuitId.equals(that.circuitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(circuitId);
    }
}
