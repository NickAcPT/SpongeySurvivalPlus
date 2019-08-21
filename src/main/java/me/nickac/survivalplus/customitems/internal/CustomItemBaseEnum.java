package me.nickac.survivalplus.customitems.internal;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public enum CustomItemBaseEnum {
    DIAMOND_HOE(1561, ItemTypes.DIAMOND_HOE, "items/diamond_hoe", "item/diamond_hoe"),
    DIAMOND_AXE(1561, ItemTypes.DIAMOND_AXE, "items/diamond_axe", "item/diamond_axe"),
    DIAMOND_PICKAXE(1561, ItemTypes.DIAMOND_PICKAXE, "items/diamond_pickaxe", "item/diamond_pickaxe");

    private final int maxDamage;
    private final ItemType itemType;
    private final String modelLoc;
    private final String textureLoc;

    CustomItemBaseEnum(int maxDamage, ItemType itemType, String textureLoc, String modelLoc) {
        this.maxDamage = maxDamage;
        this.itemType = itemType;
        this.textureLoc = textureLoc;
        this.modelLoc = modelLoc;
    }

    public static CustomItemBaseEnum getForCountedItem(int count) {
        int innerCount = 0;
        for (CustomItemBaseEnum val : CustomItemBaseEnum.values()) {
            if (count > innerCount && count <= val.getMaxDamage())
                return val;
            innerCount += val.maxDamage;
        }
        return CustomItemBaseEnum.values()[CustomItemBaseEnum.values().length - 1];
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public String getModelLoc() {
        return modelLoc;
    }

    public String getTextureLoc() {
        return textureLoc;
    }
}
