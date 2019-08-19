package me.nickac.survivalplus;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.resourcepack.ResourcePacks;

@Plugin(
        id = "survivalplus",
        name = "SurvivalPlus"
)
public class SurvivalPlus {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

    }

    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join event, @First Player pl) {
        pl.getInventory().offer(ItemStack.builder()
                .itemType(ItemTypes.DIAMOND_HOE)
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.ITEM_DURABILITY, 1561 - 3)
                .build());
    }
}
