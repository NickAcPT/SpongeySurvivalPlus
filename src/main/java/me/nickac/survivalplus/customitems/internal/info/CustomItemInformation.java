package me.nickac.survivalplus.customitems.internal.info;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.data.*;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;

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
    private int requiredPower;
    private String modelAsset;
    private Class<? extends CustomItem> itemClass;

    public CustomItemInformation(int ordinal, String name, int requiredPower, String modelAsset, Class<?
            extends CustomItem> itemClass) {
        this.ordinal = ordinal;
        this.name = name;
        this.requiredPower = requiredPower;
        this.modelAsset = modelAsset;
        this.itemClass = itemClass;
    }

    public static CustomItemInformationBuilder builder() {
        return injector.getInstance(CustomItemInformationBuilder.class);
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
        return itemBase;
    }

    public ItemStack getNewItemStack(int amount) {
        ItemStack itemStack = itemManager.generateCustomItemStack(ordinal);
        itemStack.setQuantity(amount);
        return itemStack;
    }

    public int getRequiredPower() {
        return requiredPower;
    }

    public void setRequiredPower(int requiredPower) {
        this.requiredPower = requiredPower;
    }

    public Asset getModelAsset() {
        return plugin.getAsset(modelAsset).orElse(null);
    }

    public void setModelAsset(String modelAsset) {
        this.modelAsset = modelAsset;
    }

    public String getModelAssetRaw() {
        return modelAsset;
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
        if (name != null)
            container.set(Queries.NAME, name);
        if (modelAsset != null)
            container.set(Queries.MODEL_ASSET, modelAsset);
        container.set(Queries.ORDINAL, ordinal);
        container.set(Queries.REQUIRED_POWER, requiredPower);
        if (itemClass != null)
            container.set(Queries.ITEM_CLASS, itemClass.getName());
        return container;
    }

    @SuppressWarnings("unchecked")
    public void fromView(DataView dataView) {
        setName(dataView.getString(Queries.NAME).orElse(""));
        setModelAsset(dataView.getString(Queries.MODEL_ASSET).orElse(""));
        setOrdinal(dataView.getInt(Queries.ORDINAL).orElse(0));
        setRequiredPower(dataView.getInt(Queries.REQUIRED_POWER).orElse(0));
        try {
            itemClass = (Class<? extends CustomItem>) Class.forName(dataView.getString(Queries.ITEM_CLASS).orElse(""));
        } catch (ClassNotFoundException e) {
        }
    }

    public static class Queries {
        public final static DataQuery ORDINAL = DataQuery.of("Ordinal");
        public final static DataQuery NAME = DataQuery.of("Name");
        public final static DataQuery MODEL_ASSET = DataQuery.of("ModelAsset");
        public final static DataQuery REQUIRED_POWER = DataQuery.of("RequiredPower");
        public final static DataQuery ITEM_CLASS = DataQuery.of("ItemClass");
    }

}
