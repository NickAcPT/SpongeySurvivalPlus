package me.nickac.survivalplus.data.impl;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
public class CustomItemInfoData extends AbstractSingleData<CustomItemInformation, CustomItemInfoData,
        CustomItemInfoData.Immutable> {
    protected CustomItemInfoData(CustomItemInformation value) {
        super(value, CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE);
    }

    @Override
    public Value<CustomItemInformation> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE, getValue());
    }

    @Override
    public Optional<CustomItemInfoData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<CustomItemInfoData> data_ = dataHolder.get(CustomItemInfoData.class);
        if (data_.isPresent()) {
            CustomItemInfoData data = data_.get();
            CustomItemInfoData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<CustomItemInfoData> from(DataContainer container) {
        return Optional.of(this);
    }

    @Override
    public CustomItemInfoData copy() {
        return new CustomItemInfoData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static class Immutable extends AbstractImmutableSingleData<CustomItemInformation, Immutable,
            CustomItemInfoData> {
        Immutable(CustomItemInformation value) {
            super(value, CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE);
        }

        @Override
        public ImmutableValue<CustomItemInformation> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE,
                    getValue()).asImmutable();
        }

        @Override
        public CustomItemInfoData asMutable() {
            return new CustomItemInfoData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }
    }

    public static class Builder extends AbstractDataBuilder<CustomItemInfoData> implements DataManipulatorBuilder<CustomItemInfoData, Immutable> {
        public Builder() {
            super(CustomItemInfoData.class, 1);
        }

        @Inject
        private static CustomItemManager itemManager;

        @Override
        public CustomItemInfoData create() {
            return new CustomItemInfoData(CustomItemInformation.builder().build());
        }

        @Override
        public Optional<CustomItemInfoData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<CustomItemInfoData> buildContent(DataView container) throws InvalidDataException {
            CustomItemInfoData info = create();

            final DataView data =
                    (DataView) container.get(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE.getQuery()).get();
            final Integer ordinal = data.getInt(CustomItemInformation.Queries.ORDINAL).orElse(0);

            info.setValue(itemManager.getRegisteredItemInfoForOrdinal(ordinal));
            return Optional.of(info);
        }
    }

}