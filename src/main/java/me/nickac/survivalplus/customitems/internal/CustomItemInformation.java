package me.nickac.survivalplus.customitems.internal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
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

    public static Builder builder() {
        return injector.getInstance(Builder.class);
    }

    public Class<? extends CustomItem> getItemClass() {
        return itemClass;
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
        return container;
    }

    public void fromView(DataView dataView) {
        setName(dataView.getString(Queries.NAME).orElse(""));
        setModelAsset(dataView.getString(Queries.MODEL_ASSET).orElse(""));
        setOrdinal(dataView.getInt(Queries.ORDINAL).orElse(0));
        setRequiredPower(dataView.getInt(Queries.REQUIRED_POWER).orElse(0));
    }

    static class Queries {
        static DataQuery ORDINAL = DataQuery.of("Ordinal");
        static DataQuery NAME = DataQuery.of("Name");
        static DataQuery MODEL_ASSET = DataQuery.of("ModelAsset");
        static DataQuery REQUIRED_POWER = DataQuery.of("RequiredPower");
    }

    public static class Builder {
        private int ordinal;
        private String name;
        private int requiredPower;
        private String modelAsset;
        private Class<? extends CustomItem> itemClass;

        public Builder ordinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder named(String name) {
            this.name = name;
            return this;
        }

        public Builder requiringPower(int requiredPower) {
            this.requiredPower = requiredPower;
            return this;
        }

        public Builder withModel(String modelAsset) {
            this.modelAsset = modelAsset;
            return this;
        }

        public Builder ownedBy(Class<? extends CustomItem> itemClass) {
            this.itemClass = itemClass;
            return this;
        }

        public CustomItemInformation build() {
            return new CustomItemInformation(ordinal, name, requiredPower, modelAsset, itemClass);
        }
    }
}
