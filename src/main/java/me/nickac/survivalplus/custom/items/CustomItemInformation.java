package me.nickac.survivalplus.custom.items;

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

@SuppressWarnings("WeakerAccess")
public class CustomItemInformation implements DataSerializable {
    @Inject
    private static CustomItemManager itemManager;
    @Inject
    private static PluginContainer plugin;
    @Inject
    private static Injector injector;
    private int ordinal = 0;
    private CustomItemBaseEnum itemBase;
    private String name;
    private int requiredPower;
    private String modelAsset;

    public CustomItemInformation(int ordinal, String name, int requiredPower, String modelAsset) {
        this.ordinal = ordinal;
        this.name = name;
        this.requiredPower = requiredPower;
        this.modelAsset = modelAsset;
    }

    public static Builder builder() {
        return injector.getInstance(Builder.class);
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

    public ItemStack getNewStack() {
        return getNewStack(1);
    }

    public ItemStack getNewStack(int amount) {
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
        return 0;
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
        @Inject
        private static PluginContainer plugin;

        private int ordinal;
        private String name = "";
        private int requiredPower;
        private String modelAsset = "";
        private CustomItemBaseEnum itemBase;

        public Builder setModelAsset(String modelAsset) {
            this.modelAsset = modelAsset;
            return this;
        }

        public Builder setOrdinal(int ordinal) {
            this.ordinal = ordinal;
            this.itemBase = CustomItemBaseEnum.getForCountedItem(ordinal);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setRequiredPower(int requiredPower) {
            this.requiredPower = requiredPower;
            return this;
        }

        public CustomItemInformation build() {
            CustomItemInformation info = new CustomItemInformation(ordinal, name, requiredPower, modelAsset);
            info.itemBase = this.itemBase;
            return info;
        }
    }
}
