package cofh.redstoneflux.api.impl;

import cofh.redstoneflux.api.IEnergyStorage;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;

public class EnergyStorage implements IEnergyStorage {
    private int storedEnergy;
    private int maxEnergy;

    public EnergyStorage(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        final int current = getEnergyStored();
        int receive = maxReceive - ((current + maxReceive) - Math.min(current + maxReceive,
                maxEnergy));

        if (!simulate) storedEnergy += receive;
        return receive;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        final int toRemove = Math.min(maxExtract, storedEnergy);
        if (!simulate) storedEnergy -= toRemove;

        return toRemove;
    }

    @Override
    public int getEnergyStored() {
        return storedEnergy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxEnergy;
    }

    public void saveInfo(DataView view) {
        view.set(DataQuery.of("Energy"), storedEnergy);
    }

    public void loadInfo(DataView view) {
        storedEnergy = view.getInt(DataQuery.of("Energy")).orElse(0);
    }
}
