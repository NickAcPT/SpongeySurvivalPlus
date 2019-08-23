package me.nickac.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyProvider;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import org.spongepowered.api.util.Direction;

public class CoalGeneratorBlock extends CustomBlock implements IEnergyProvider {

    @Override
    public int extractEnergy(Direction from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored(Direction from) {
        return 0;
    }

    @Override
    public int getMaxEnergyStored(Direction from) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(Direction from) {
        return false;
    }
}
