package io.github.nickacpt.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyHandler;
import com.google.inject.Inject;
import io.github.nickacpt.survivalplus.customitems.internal.CustomItem;
import io.github.nickacpt.survivalplus.energy.EnergyMap;
import io.github.nickacpt.survivalplus.customitems.internal.CustomBlock;
import io.github.nickacpt.survivalplus.customitems.internal.info.CustomItemInformation;
import io.github.nickacpt.survivalplus.energy.EnergyCircuit;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BlockDebuggerItem extends CustomItem {

    @Inject
    private static EnergyMap energyMap;

    @Override
    public void onInteract(Player player, InteractBlockEvent event) {
        final BlockSnapshot block = event.getTargetBlock();

        if (itemManager.isManagedBlock(block)) {
            final CustomBlock info = itemManager.getManagedBlockInfo(block);
            List<Text> texts = new ArrayList<>();
            final CustomItemInformation itemInfo = info.getInfo();
            if (info instanceof IEnergyHandler) {
                texts.add(
                        Text.of(TextColors.GOLD, "Stored Energy: ", TextColors.GREEN, ((IEnergyHandler) info).getEnergyStored(), " RF"));
            }

            final Optional<EnergyCircuit> circuit = energyMap.getCircuitOfBlock(info);
            if (circuit.isPresent()) {
                final EnergyCircuit energyCircuit = circuit.get();
                texts.add(Text.of(TextColors.GOLD, "Energy Circuit ID: ", TextColors.GREEN,
                        energyCircuit.getCircuitId()));
                texts.add(Text.of(TextColors.GOLD, "Block Count on Circuit: ", TextColors.GREEN,
                        energyCircuit.getBlocks().size()));
                texts.add(Text.of(TextColors.GOLD, "Current Energy on Circuit from Providers: ", TextColors.GREEN,
                        energyCircuit.getStoredEnergy(), " RF"));
            }

            texts.add(Text.of(TextColors.GOLD, "Block Id: ", TextColors.GREEN, itemInfo.getOrdinal()));
            texts.add(Text.of(TextColors.GOLD, "Item Base: ", TextColors.GREEN, itemInfo.getItemBase().name()));
            texts.add(Text.of(TextColors.GOLD, "Class Name: ", TextColors.GREEN, info.getClass().getSimpleName()));
            texts.add(Text.of(TextColors.GOLD, "Model Name: ", TextColors.GREEN, itemInfo.getModelAssetRaw()));

            for (int i = 0; i < 15; i++) player.sendMessage(Text.EMPTY);

            PaginationList.builder().title(Text.of("SurvivalPlus - Block Debugger"))
                    .contents(texts)
                    .padding(Text.of("-"))
                    .sendTo(player);

        }
    }
}
