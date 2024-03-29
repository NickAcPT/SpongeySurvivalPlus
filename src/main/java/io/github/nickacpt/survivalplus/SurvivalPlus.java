package io.github.nickacpt.survivalplus;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.nickacpt.survivalplus.customitems.*;
import io.github.nickacpt.survivalplus.customitems.internal.CustomItemBaseEnum;
import io.github.nickacpt.survivalplus.customitems.internal.events.CustomBlocksEventListener;
import io.github.nickacpt.survivalplus.customitems.internal.info.CustomItemInformation;
import io.github.nickacpt.survivalplus.energy.EnergyMap;
import io.github.nickacpt.survivalplus.managers.CustomItemManager;
import io.github.nickacpt.survivalplus.managers.ResourcePackManager;
import io.github.nickacpt.survivalplus.misc.SurvivalPlusModule;
import io.github.nickacpt.survivalplus.misc.data.CustomKeys;
import io.github.nickacpt.survivalplus.misc.data.impl.CustomItemData;
import io.github.nickacpt.survivalplus.misc.data.impl.CustomItemInfoData;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.event.world.chunk.LoadChunkEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.BookView;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.nio.file.Path;

import static org.spongepowered.api.item.inventory.query.QueryOperationTypes.INVENTORY_PROPERTY;

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
    private EnergyMap energyMap;
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
        energyMap = injector.getInstance(EnergyMap.class);
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
                .withModel("wires/wire.json")
                .ownedBy(WireBlock.class)
                .build());

        registerWires();

        itemManager.registerItem(CustomItemInformation.builder()
                .named("Power Bank")
                .withModel("power_bank.json")
                .ownedBy(PowerBankBlock.class)
                .build());

        itemManager.registerItem(CustomItemInformation.builder()
                .named("Block Debugger")
                .withModel("block_debugger.json")
                .ownedBy(BlockDebuggerItem.class)
                .build());

        itemManager.registerItem(CustomItemInformation.builder()
                .named("Energy Map Inspector")
                .withModel("energy_map_inspector.json")
                .ownedBy(EnergyMapInspectorItem.class)
                .build());
    }

    private void registerWires() {
        registerCustomWire("n");
        registerCustomWire("s");
        registerCustomWire("w");
        registerCustomWire("e");
        registerCustomWire("u");
        registerCustomWire("d");
        registerCustomWire("ns");
        registerCustomWire("nw");
        registerCustomWire("ne");
        registerCustomWire("nu");
        registerCustomWire("nd");
        registerCustomWire("sw");
        registerCustomWire("se");
        registerCustomWire("su");
        registerCustomWire("sd");
        registerCustomWire("we");
        registerCustomWire("wu");
        registerCustomWire("wd");
        registerCustomWire("eu");
        registerCustomWire("ed");
        registerCustomWire("ud");
        registerCustomWire("nsw");
        registerCustomWire("nse");
        registerCustomWire("nsu");
        registerCustomWire("nsd");
        registerCustomWire("nwe");
        registerCustomWire("nwu");
        registerCustomWire("nwd");
        registerCustomWire("neu");
        registerCustomWire("ned");
        registerCustomWire("nud");
        registerCustomWire("swe");
        registerCustomWire("swu");
        registerCustomWire("swd");
        registerCustomWire("seu");
        registerCustomWire("sed");
        registerCustomWire("sud");
        registerCustomWire("weu");
        registerCustomWire("wed");
        registerCustomWire("wud");
        registerCustomWire("eud");
        registerCustomWire("nswe");
        registerCustomWire("nswu");
        registerCustomWire("nswd");
        registerCustomWire("nseu");
        registerCustomWire("nsed");
        registerCustomWire("nsud");
        registerCustomWire("nweu");
        registerCustomWire("nwed");
        registerCustomWire("nwud");
        registerCustomWire("neud");
        registerCustomWire("sweu");
        registerCustomWire("swed");
        registerCustomWire("swud");
        registerCustomWire("seud");
        registerCustomWire("weud");
        registerCustomWire("nsweu");
        registerCustomWire("nswed");
        registerCustomWire("nswud");
        registerCustomWire("nseud");
        registerCustomWire("nweud");
        registerCustomWire("sweud");
        registerCustomWire("nsweud");
    }

    private void registerCustomWire(String orientation) {
        itemManager.registerItem(CustomItemInformation.builder()
                .named(String.format("Wire (%s)", orientation.toUpperCase()))
                .withModel(String.format("wires/wire-%s.json", orientation))
                .ownedBy(WireBlock.class)
                .internal()
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
    public void onLoadChunk(LoadChunkEvent event) {
        energyMap.processQueuedBlocks();
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
                .executor((src, args) -> {
                    if (!(src instanceof Player)) return CommandResult.empty();

                    final LiteralText.Builder builder =
                            Text.builder("Click to get custom item:").append(Text.NEW_LINE).append(Text.NEW_LINE);

                    itemManager.getRegisteredItems().stream()
                            .filter(c -> !c.isInternal())
                            .forEach(i -> builder.append(Text.builder().append(Text.of(TextColors.BLUE,
                                    TextStyles.UNDERLINE, i.getName()))
                                    .onHover(TextActions.showText(Text.of(TextColors.DARK_GREEN, "ID: ",
                                            TextColors.GOLD, i.getOrdinal())))
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

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    if (!(src instanceof Player)) return CommandResult.empty();

                    PaginationList.builder()
                            .contents(energyMap.getCircuits().stream().map(circuit -> {
                                return Text.builder("Circuit ID: ").color(TextColors.GOLD)
                                        .append(Text.of(TextColors.DARK_GREEN, circuit.getCircuitId().toString()))
                                        .build();
                            }).toArray(Text[]::new))
                            .title(Text.of("SurvivalPlus - Energy Map"))
                            .padding(Text.of("="))
                            .sendTo(src);

                    return CommandResult.success();
                })
                .build(), "energymap", "circuitmap");

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .executor((src, args) -> {
                    if (!(src instanceof Player)) return CommandResult.empty();

                    final Inventory inv = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
                            .property(InventoryTitle.of(Text.of("Something")))
                            .listener(ClickInventoryEvent.class, evt -> {
                                //evt.setCancelled(true);
                                evt.getTransactions().forEach(slotTransaction -> {
                                    System.out.println("Transaction");
                                });
                            })
                            .listener(InteractInventoryEvent.class, evt2 -> {
                                System.out.println("reee");
                                if (evt2 instanceof ClickInventoryEvent.Shift) {
                                    System.out.println("shift move");
                                }
                            })
                            .build(container);

                    inv.query(INVENTORY_PROPERTY.of(SlotPos.of(0, 0))).offer(getGenericItemStackForCustomItem(74));
                    inv.query(INVENTORY_PROPERTY.of(SlotPos.of(0, 5))).offer(getGenericItemStackForCustomItem(73));

                    ((Player) src).openInventory(inv);


                    return CommandResult.success();
                })
                .build(), "invtest");

        Sponge.getCommandManager().register(this, CommandSpec.builder()
                .arguments(GenericArguments.integer(Text.of("count")))
                .executor((src, args) -> {
                    int count = args.<Integer>getOne(Text.of("count")).orElse(0);

                    if (src instanceof Player) {

                        ItemStack stack = getGenericItemStackForCustomItem(count);
                        ((Player) src).getInventory().offer(stack);

                    }
                    return CommandResult.successCount(count);
                })
                .build(), "debugcustomitem");
    }

    private ItemStack getGenericItemStackForCustomItem(int count) {
        CustomItemBaseEnum countedItem = CustomItemBaseEnum.getForCountedItem(count);

        DataContainer dataContainer = ItemStack.builder()
                .itemType(countedItem.getItemType())
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.ITEM_DURABILITY,
                        countedItem.getMaxDamage() - (count % countedItem.getMaxDamage()))
                .build().toContainer();

        return ItemStack.builder().fromContainer(dataContainer).build();
    }

}
