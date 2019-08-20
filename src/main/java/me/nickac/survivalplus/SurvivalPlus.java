package me.nickac.survivalplus;

import com.google.inject.Inject;
import me.nickac.survivalplus.custom.CustomBlocksEventListener;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(
        id = "survivalplus",
        name = "SurvivalPlus"
)
public class SurvivalPlus {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path pluginPath;

    @Inject
    private CustomItemManager itemManager;

    @Inject
    private CustomBlocksEventListener customBlocksEventListener;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        Sponge.getEventManager().registerListeners(this, customBlocksEventListener);
        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .arguments(GenericArguments.integer(Text.of("count")))
                .executor((src, args) -> {
                    int count = args.<Integer>getOne(Text.of("count")).orElse(0);

                    if (src instanceof Player) {
                        ((Player) src).getInventory().offer(itemManager.generateCustomItemStack(count));
                    }

                    return CommandResult.successCount(count);
                })
                .build(), "debugcustomitem");

    }

    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join event, @First Player pl) {
//        pl.getInventory().offer(itemManager.generateCustomItemStack(1));

    }
}
