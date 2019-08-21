package me.nickac.survivalplus.customitems;

import me.nickac.survivalplus.customitems.internal.CustomBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RandomSandBlock extends CustomBlock {
    @Override
    public void onBlockInteract(Player player, InteractBlockEvent event) {
        if (event instanceof InteractBlockEvent.Secondary.MainHand) {
            player.sendMessage(Text.of(TextColors.GOLD, "You clicked me!"));
        }
    }
}
