package me.nickac.survivalplus.customitems;

import cofh.redstoneflux.api.IEnergyHandler;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockDebuggerItem extends CustomItem {

    @Override
    public void onInteract(Player player, InteractBlockEvent event) {
        final BlockSnapshot block = event.getTargetBlock();

        if (itemManager.isManagedBlock(block)) {
            final CustomBlock info = itemManager.getManagedBlockInfo(block);
            List<Text> texts = new ArrayList<>();
            final CustomItemInformation itemInfo = info.getInfo();
            texts.add(Text.of(TextColors.GOLD, "Block Id: ", TextColors.GREEN, itemInfo.getOrdinal()));
            texts.add(Text.of(TextColors.GOLD, "Item Base: ", TextColors.GREEN, itemInfo.getItemBase().name()));
            texts.add(Text.of(TextColors.GOLD, "Class Name: ", TextColors.GREEN, info.getClass().getSimpleName()));
            texts.add(Text.of(TextColors.GOLD, "Model Name: ", TextColors.GREEN, itemInfo.getModelAssetRaw()));
            if (info instanceof IEnergyHandler) {

                texts.add(Text.of(TextColors.GOLD, "Stored Energy: ",
                        Text
                                .builder("Click to read values")
                                .color(TextColors.GREEN)
                                .style(TextStyles.UNDERLINE)
                                .onClick(TextActions.executeCallback(src -> {
                                    PaginationList.builder().title(Text.of("Block Debugger - Energy Data"))
                                            .contents(Arrays.stream(Direction.values())
                                                    .filter(d -> d.isCardinal() && !d.isSecondaryOrdinal())
                                                    .map(dir -> Text.of(TextColors.GOLD, "Energy Stored on ",
                                                            dir.name().toLowerCase(), ": ",
                                                            ((IEnergyHandler) info).getEnergyStored(dir)))
                                                    .collect(Collectors.toList()))
                                            .linesPerPage(6)
                                            .padding(Text.of("-"))
                                            .sendTo(src);
                                }))
                        )
                );
            }

            PaginationList.builder().title(Text.of("SurvivalPlus - Block Debugger"))
                    .contents(texts)
                    .linesPerPage(6)
                    .padding(Text.of("-"))
                    .sendTo(player);

        }
    }
}
