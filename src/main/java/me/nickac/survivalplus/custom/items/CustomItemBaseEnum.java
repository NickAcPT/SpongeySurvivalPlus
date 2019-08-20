package me.nickac.survivalplus.custom.items;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public enum CustomItemBaseEnum {
    DIAMOND_HOE(1561, ItemTypes.DIAMOND_HOE),
    DIAMOND_AXE(1561, ItemTypes.DIAMOND_AXE),
    DIAMOND_PICKAXE(1561, ItemTypes.DIAMOND_PICKAXE);

    private final int maxDamage;
    private final ItemType itemType;

    CustomItemBaseEnum(int maxDamage, ItemType itemType) {
        this.maxDamage = maxDamage;
        this.itemType = itemType;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getMaxDamage() {
        return maxDamage;
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
}
