package me.nickac.survivalplus.data;

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

public class ManagedTypeData extends AbstractSingleData<Boolean, ManagedTypeData, ManagedTypeData.Immutable> {
    protected ManagedTypeData(Boolean value) {
        super(value, CustomKeys.MANAGED_TYPE);
    }

    public Value<Boolean> isManaged() {
        return getValueGetter();
    }

    @Override
    protected Value<Boolean> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.MANAGED_TYPE, getValue());
    }

    @Override
    public Optional<ManagedTypeData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<ManagedTypeData> data_ = dataHolder.get(ManagedTypeData.class);
        if (data_.isPresent()) {
            ManagedTypeData data = data_.get();
            ManagedTypeData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);

    }

    @Override
    public Optional<ManagedTypeData> from(DataContainer container) {
        return Optional.of(this);
    }

    @Override
    public ManagedTypeData copy() {
        return new ManagedTypeData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static class Immutable extends AbstractImmutableSingleData<Boolean, Immutable, ManagedTypeData> {
        Immutable(Boolean value) {
            super(value, CustomKeys.MANAGED_TYPE);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.MANAGED_TYPE, getValue()).asImmutable();
        }

        @Override
        public ManagedTypeData asMutable() {
            return new ManagedTypeData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }
    }

    public static class Builder extends AbstractDataBuilder<ManagedTypeData> implements DataManipulatorBuilder<ManagedTypeData, Immutable> {
        public Builder() {
            super(ManagedTypeData.class, 1);
        }

        @Override
        public ManagedTypeData create() {
            return new ManagedTypeData(true);
        }

        @Override
        public Optional<ManagedTypeData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<ManagedTypeData> buildContent(DataView container) throws InvalidDataException {
            return Optional.of(create());
        }
    }

}
