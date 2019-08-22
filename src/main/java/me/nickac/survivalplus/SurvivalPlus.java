package me.nickac.survivalplus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.nickac.survivalplus.customitems.CoalGeneratorBlock;
import me.nickac.survivalplus.customitems.WireBlock;
import me.nickac.survivalplus.customitems.internal.events.CustomBlocksEventListener;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.data.impl.CustomItemData;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;
import me.nickac.survivalplus.managers.CustomItemManager;
import me.nickac.survivalplus.managers.ResourcePackManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.nio.file.Path;

@Plugin(
        id = "survivalplus",
        name = "SurvivalPlus",
        version = "1.0-SNAPSHOT"
)
public class SurvivalPlus {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path pluginPath;

    private CustomItemManager itemManager;
    private ResourcePackManager resourcePackManager;

    @Inject
    private Injector injector;

    @Inject
    private CustomBlocksEventListener customBlocksEventListener;

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        prepareStuff();
        registerKeys();
        registerCustomItems();
    }

    private void prepareStuff() {
        injector = injector.createChildInjector(new SurvivalPlusModule());
        itemManager = injector.getInstance(CustomItemManager.class);
        Sponge.getEventManager()
                .registerListeners(this, resourcePackManager = injector.getInstance(ResourcePackManager.class));
    }

    private void registerCustomItems() {
        itemManager.registerItem(CustomItemInformation.builder()
                .named("Coal Generator")
                .ordinal(1)
                .directional()
                .withModel("coal_generator.json")
                .ownedBy(CoalGeneratorBlock.class)
                .build());
        itemManager.registerItem(CustomItemInformation.builder()
                .named("Wire")
                .ordinal(2)
                .withModel("wire.json")
                .ownedBy(WireBlock.class)
                .build());
    }

    private void registerKeys() {
        CustomKeys.dummy();

        DataRegistration.builder()
                .dataName("Managed Type")
                .manipulatorId("custom_item_information")
                .dataClass(CustomItemInfoData.class)
                .immutableClass(CustomItemInfoData.Immutable.class)
                .builder(new CustomItemInfoData.Builder())
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataName("Custom Item")
                .manipulatorId("custom_item")
                .dataClass(CustomItemData.class)
                .immutableClass(CustomItemData.Immutable.class)
                .builder(new CustomItemData.Builder())
                .buildAndRegister(container);
    }

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

}
