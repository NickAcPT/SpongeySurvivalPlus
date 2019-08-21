package me.nickac.survivalplus.data;

import com.google.common.reflect.TypeToken;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

@SuppressWarnings("UnstableApiUsage")
public class CustomKeys {

    public static final Key<Value<CustomItemInformation>> CUSTOM_ITEM_INFORMATION_VALUE;
    public static final Key<Value<CustomItem>> CUSTOM_ITEM_VALUE;

    static {
        CUSTOM_ITEM_INFORMATION_VALUE = Key.builder()
                .type(new TypeToken<Value<CustomItemInformation>>() {
                })
                .id("custom_item_information")
                .name("Custom Item Information")
                .query(DataQuery.of('.', "custom_item_information"))
                .build();
        CUSTOM_ITEM_VALUE = Key.builder()
                .type(new TypeToken<Value<CustomItem>>() {
                })
                .id("custom_item")
                .name("Custom Item Instance")
                .query(DataQuery.of('.', "custom_item"))
                .build();

    }

    private CustomKeys() {
    }

    public static void dummy() {
    } // invoke static constructor
}
