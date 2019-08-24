package me.nickac.survivalplus.customitems.internal.info;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Direction;

import java.util.HashMap;
import java.util.Map;

/**
 * Information about the custom item (or block). Not used on the mob spawners
 */
@SuppressWarnings("WeakerAccess")
public class CustomItemInformation implements DataSerializable {
    @Inject
    private static CustomItemManager itemManager;
    @Inject
    private static PluginContainer plugin;
    @Inject
    private static Injector injector;
    private int ordinal;
    private CustomItemBaseEnum itemBase;
    private String name;
    private boolean directional;
    private boolean internal;
    private String modelAsset;
    private Class<? extends CustomItem> itemClass;
    private Asset customAsset = null;
    private Map<Direction, CustomItemInformation> directions = new HashMap<>();

    public CustomItemInformation(int ordinal, String name, boolean directional, String modelAsset, Class<?
            extends CustomItem> itemClass, boolean internal) {
        this.ordinal = ordinal;
        this.name = name;
        this.directional = directional;
        this.modelAsset = modelAsset;
        this.itemClass = itemClass;
        this.internal = internal;
    }

    public void addDirectionalInfo(Direction dir, CustomItemInformation i) {
        directions.put(dir, i);
    }

    public CustomItemInformation getDirectionalInfo(Direction dir) {
        return directions.getOrDefault(dir, this);
    }

    public static CustomItemInformationBuilder builder() {
        return injector.getInstance(CustomItemInformationBuilder.class);
    }

    public CustomItemInformation setCustomAsset(Asset customAsset) {
        this.customAsset = customAsset;
        return this;
    }

    public Class<? extends CustomItem> getItemClass() {
        return itemClass;
    }

    public CustomItem createNewInstance() {
        CustomItem instance = injector.getInstance(itemClass);
        if (instance != null)
            instance.setInfo(this);
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
        if (itemBase == null)
            itemBase = CustomItemBaseEnum.getForCountedItem(ordinal);
    }

    public CustomItemBaseEnum getItemBase() {
        if (itemBase == null)
            itemBase = CustomItemBaseEnum.getForCountedItem(ordinal);
        return itemBase;
    }

    public boolean isInternal() {
        return internal;
    }

    public ItemStack getNewItemStack(int amount) {
        ItemStack itemStack = itemManager.generateCustomItemStack(ordinal);
        itemStack.setQuantity(amount);
        return itemStack;
    }

    public boolean isDirectional() {
        return directional;
    }

    public void setIsDirectional(boolean directional) {
        this.directional = directional;
    }

    public Asset getModelAsset() {
        return plugin.getAsset(modelAsset).orElse(customAsset);
    }

    public void setModelAsset(String modelAsset) {
        this.modelAsset = modelAsset;
    }

    public String getModelAssetRaw() {
        return modelAsset;
    }


    public String getModelAssetRawFile() {
        return getModelAssetRaw().substring(Math.max(0, getModelAssetRaw().lastIndexOf('/') + 1));
    }

    public boolean isBlock() {
        return CustomBlock.class.isAssignableFrom(itemClass);
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew();
        container.set(Queries.ORDINAL, ordinal);
        if (itemClass != null)
            container.set(Queries.ITEM_CLASS, itemClass.getName());
        return container;
    }

    public static class Queries {
        public final static DataQuery ORDINAL = DataQuery.of("Ordinal");
        public final static DataQuery ITEM_CLASS = DataQuery.of("ItemClass");
    }

}
