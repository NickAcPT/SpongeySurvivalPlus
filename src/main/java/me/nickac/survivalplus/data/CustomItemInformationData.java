package me.nickac.survivalplus.data;

import me.nickac.survivalplus.custom.items.CustomItemInformation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.*;
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
    public class CustomItemInformationData extends AbstractSingleData<CustomItemInformation, CustomItemInformationData, CustomItemInformationData.Immutable> {
    protected CustomItemInformationData(CustomItemInformation value) {
        super(value, CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE);
    }

    @Override
    public Value<CustomItemInformation> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE, getValue());
    }

    @Override
    public Optional<CustomItemInformationData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<CustomItemInformationData> data_ = dataHolder.get(CustomItemInformationData.class);
        if (data_.isPresent()) {
            CustomItemInformationData data = data_.get();
            CustomItemInformationData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);

    }

    @Override
    public Optional<CustomItemInformationData> from(DataContainer container) {
        return Optional.of(this);
    }

    @Override
    public CustomItemInformationData copy() {
        return new CustomItemInformationData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static class Immutable extends AbstractImmutableSingleData<CustomItemInformation, Immutable, CustomItemInformationData> {
        Immutable(CustomItemInformation value) {
            super(value, CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE);
        }

        @Override
        public ImmutableValue<CustomItemInformation> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE, getValue()).asImmutable();
        }

        @Override
        public CustomItemInformationData asMutable() {
            return new CustomItemInformationData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }
    }

    public static class Builder extends AbstractDataBuilder<CustomItemInformationData> implements DataManipulatorBuilder<CustomItemInformationData, Immutable> {
        public Builder() {
            super(CustomItemInformationData.class, 1);
        }

        @Override
        public CustomItemInformationData create() {
            return new CustomItemInformationData(CustomItemInformation.builder().build());
        }

        @Override
        public Optional<CustomItemInformationData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<CustomItemInformationData> buildContent(DataView container) throws InvalidDataException {
            CustomItemInformationData obj = create();
            obj.getValueGetter().get().fromView(((DataView)container.get(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE.getQuery()).get()));
            return Optional.of(obj);
        }
    }

}