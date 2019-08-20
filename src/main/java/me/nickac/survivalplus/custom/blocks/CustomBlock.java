package me.nickac.survivalplus.custom.blocks;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

public class CustomBlock {

    public boolean canPlace(Player player) {
        return true;
    }

    public boolean canBreak(Player player) {
        return true;
    }

}
