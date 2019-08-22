package me.nickac.survivalplus.customitems.internal.info;

import me.nickac.survivalplus.customitems.internal.CustomItem;

public class CustomItemInformationBuilder {
    private int ordinal;
    private String name;
    private boolean directional;
    private String modelAsset;
    private Class<? extends CustomItem> itemClass;

    public CustomItemInformationBuilder ordinal(int ordinal) {
        this.ordinal = ordinal;
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
        return new CustomItemInformation(ordinal, name, directional, modelAsset, itemClass);
    }

    public CustomItemInformationBuilder directional() {
        directional = true;
        return this;
    }
}
