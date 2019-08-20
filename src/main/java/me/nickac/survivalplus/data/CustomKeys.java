package me.nickac.survivalplus.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class CustomKeys {
    private CustomKeys() {
    }

    public static void dummy() {} // invoke static constructor
    public static final Key<Value<Boolean>> MANAGED_TYPE;

    static {
            MANAGED_TYPE = Key.builder()
                .type(TypeTokens.BOOLEAN_VALUE_TOKEN)
                .id("managed_type")
                .name("Is Managed")
                .query(DataQuery.of('.', "managed_type"))
                .build();

    }
}
