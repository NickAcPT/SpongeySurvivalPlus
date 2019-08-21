package me.nickac.survivalplus.customitems;

import me.nickac.survivalplus.customitems.internal.CustomBlock;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RandomSandBlock extends CustomBlock {
    private int count = 0;

    @Override
    public void saveInfo(DataView view) {
        view.set(DataQuery.of("ClickCount"), count);
    }

    @Override
    public void loadInfo(DataView view) {
        count = view.getInt(DataQuery.of("ClickCount")).orElse(0);
    }

    @Override
    public void onBlockInteract(Player player, InteractBlockEvent event) {
        if (event instanceof InteractBlockEvent.Secondary.MainHand) {
            count++;

            player.sendMessage(Text.of(TextColors.GOLD, "You clicked me ", TextColors.RED, count , TextColors.GOLD," times!"));
        }
    }
}
