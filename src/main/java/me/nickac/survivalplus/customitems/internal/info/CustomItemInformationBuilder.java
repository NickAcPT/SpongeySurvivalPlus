package me.nickac.survivalplus.customitems.internal.info;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.managers.CustomItemManager;

public class CustomItemInformationBuilder {
    @Inject
    private static CustomItemManager itemManager;
    private String name;
    private boolean directional;
    private String modelAsset;
    private Class<? extends CustomItem> itemClass;
    private boolean internal;

    public CustomItemInformationBuilder internal() {
        this.internal = true;
        return this;
    }

    public CustomItemInformationBuilder named(String name) {
        this.name = name;
        return this;
    }

    public CustomItemInformationBuilder withModel(String modelAsset) {
        this.modelAsset = modelAsset;
        return this;
    }

    public CustomItemInformationBuilder ownedBy(Class<? extends CustomItem> itemClass) {
        this.itemClass = itemClass;
        return this;
    }

    public CustomItemInformation build() {
        return new CustomItemInformation(itemManager.getNextOrdinal(), name, directional, modelAsset, itemClass,
                internal);
    }

    public CustomItemInformationBuilder directional() {
        directional = true;
        return this;
    }
}
