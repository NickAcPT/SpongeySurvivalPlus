package me.nickac.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyConnection;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.function.Consumer;

public class WireBlock extends CustomBlock implements IEnergyConnection {
        @Override
    public void onPrePlace(Player player, Consumer<CustomItemInformation> modifyInfo) {
        updateCableState(modifyInfo, true);
    }

    private void updateCableState(Consumer<CustomItemInformation> modifyInfo, boolean updateOthers) {
        if (getLocation() == null) {
            System.err.println("Found block without location!");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (Direction dir : ENERGY_SIDE_DIRECTIONS) {
            final Location relative = getLocation().getBlockRelative(dir.getOpposite());
            final BlockSnapshot block = relative.createSnapshot();
            if (getItemManager().isManagedBlock(block)) {
                final CustomBlock info = getItemManager().getManagedBlockInfo(block);
                if (info instanceof IEnergyConnection && ((IEnergyConnection) info).canConnectEnergy(dir.getOpposite())) {
                    final char c = dir.name().toLowerCase().charAt(0);
                    String sideWires = builder.toString();
                    final String asset = String.format("wire%s.json", !sideWires.isEmpty() ?
                            "-" + sideWires + c : "");
                    if (getItemManager().findInfoForAsset(asset).isPresent()) {
                        builder.append(c);
                    } else {
                        System.out.println(String.format("Unable to find model for [%s]", asset));
                    }
                    if (updateOthers)
                        ((IEnergyConnection) info).updateConnection();
                }
            }
        }

        String sideWires = builder.toString();

        modifyInfo.accept(getItemManager().findInfoForAsset(String.format("wire%s.json", !sideWires.isEmpty() ?
                "-" + sideWires : "")).get());
    }

    @Override
    public boolean canConnectEnergy(Direction from) {
        return true;
    }

    @Override
    public void updateConnection() {
        updateCableState(this::setInfo, false);
    }
}
