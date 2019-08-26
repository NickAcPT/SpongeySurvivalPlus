package me.nickac.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import cofh.redstoneflux.api.impl.EnergyStorage;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import org.spongepowered.api.util.Direction;

public class PowerBankBlock extends CustomBlock implements IEnergyReceiver, IEnergyProvider {
    private EnergyStorage storage;

    public PowerBankBlock() {
        storage = new EnergyStorage(100_000);
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

    @Override
    public boolean canConnectEnergy(Direction from) {
        return true;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, simulate);
    }
}
