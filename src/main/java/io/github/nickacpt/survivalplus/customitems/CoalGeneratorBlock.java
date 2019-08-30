package io.github.nickacpt.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.impl.EnergyStorage;
import io.github.nickacpt.survivalplus.customitems.internal.CustomBlock;
import io.github.nickacpt.survivalplus.customitems.internal.ITickableBlock;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.util.Direction;

public class CoalGeneratorBlock extends CustomBlock implements IEnergyProvider, ITickableBlock {
    private EnergyStorage storage = new EnergyStorage(100_000);

    @Override
    public void tick() {
        storage.receiveEnergy(5, false);
    }

    //region Energy
    @Override
    public boolean canConnectEnergy(Direction from) {
        return !from.isUpright();
    }

    @Override
    public void loadInfo(DataView view) {
        super.loadInfo(view);
        storage.loadInfo(view);
    }

    @Override
    public void saveInfo(DataView view) {
        super.saveInfo(view);
        storage.saveInfo(view);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }
    //endregion
}
