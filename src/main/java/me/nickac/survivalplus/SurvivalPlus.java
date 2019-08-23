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
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

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
        registerCustomItems();
        registerKeys();
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
                .directional()
                .withModel("coal_generator.json")
                .ownedBy(CoalGeneratorBlock.class)
                .build());
        itemManager.registerItem(CustomItemInformation.builder()
                .named("Wire")
                .withModel("wire.json")
                .ownedBy(WireBlock.class)
                .build());
    }

    private void registerKeys() {
        CustomKeys.dummy();

        DataRegistration.builder()
                .name("Managed Type")
                .id("custom_item_information")
                .dataClass(CustomItemInfoData.class)
                .immutableClass(CustomItemInfoData.Immutable.class)
                .builder(new CustomItemInfoData.Builder())
                .build();

        DataRegistration.builder()
                .name("Custom Item")
                .id("custom_item")
                .dataClass(CustomItemData.class)
                .immutableClass(CustomItemData.Immutable.class)
                .builder(new CustomItemData.Builder())
                .build();
    }

    @Listener
    public void onKeyRegister(GameRegistryEvent.Register<Key<?>> event) {
        event.register(CustomKeys.CUSTOM_ITEM_VALUE);
        event.register(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE);
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

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    if (!(src instanceof Player)) return CommandResult.empty();

                    final LiteralText.Builder builder = Text.builder("Click to get custom item:").append(Text.NEW_LINE).append(Text.NEW_LINE);

                    itemManager.getRegisteredItems().stream()
                            .filter(c -> !c.isInternal())
                            .forEach(i -> builder.append(Text.builder().append(Text.of(TextColors.BLUE, TextStyles.UNDERLINE, i.getName()))
                                    .onHover(TextActions.showText(Text.of(TextColors.DARK_GREEN, "ID: ", TextColors.GOLD, i.getOrdinal())))
                                    .onClick(TextActions.executeCallback(ii ->
                                            ((Player) src).getInventory().offer(itemManager.generateCustomItemStack(i.getOrdinal())))).build())
                                    .append(Text.NEW_LINE)
                            );

                    final BookView book = BookView.builder()
                            .author(Text.of("NickAc"))
                            .addPages(
                                    builder.build()).build();
                    ((Player) src).sendBookView(book);
                    return CommandResult.success();
                })
                .build(), "customitems");
    }

}
