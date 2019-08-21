package me.nickac.survivalplus.data.impl;

import com.google.inject.Inject;
import me.nickac.survivalplus.customitems.internal.CustomItem;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
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
public class CustomItemData extends AbstractSingleData<CustomItem, CustomItemData, CustomItemData.Immutable> {
    private CustomItemData(CustomItem value) {
        super(value, CustomKeys.CUSTOM_ITEM_VALUE);
    }

    @Override
    public Value<CustomItem> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_VALUE, getValue());
    }

    @Override
    public Optional<CustomItemData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<CustomItemData> data_ = dataHolder.get(CustomItemData.class);
        if (data_.isPresent()) {
            CustomItemData data = data_.get();
            CustomItemData finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);

    }

    @Override
    public Optional<CustomItemData> from(DataContainer container) {
        return Optional.of(this);
    }

    @Override
    public CustomItemData copy() {
        return new CustomItemData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public static class Immutable extends AbstractImmutableSingleData<CustomItem, Immutable, CustomItemData> {
        Immutable(CustomItem value) {
            super(value, CustomKeys.CUSTOM_ITEM_VALUE);
        }

        @Override
        public ImmutableValue<CustomItem> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(CustomKeys.CUSTOM_ITEM_VALUE, getValue()).asImmutable();
        }

        @Override
        public CustomItemData asMutable() {
            return new CustomItemData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }
    }

    public static class Builder extends AbstractDataBuilder<CustomItemData> implements DataManipulatorBuilder<CustomItemData, Immutable> {
        @Inject
        private static CustomItemManager itemManager;

        public Builder() {
            super(CustomItemData.class, 1);
        }

        @Override
        public CustomItemData create() {
            return new CustomItemData(CustomItem.EMPTY);
        }

        @Override
        public Optional<CustomItemData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<CustomItemData> buildContent(DataView container) throws InvalidDataException {
            CustomItemData value = create();
            if (container.contains(CustomKeys.CUSTOM_ITEM_VALUE.getQuery())) {
                final DataView dataView = (DataView) container.get(CustomKeys.CUSTOM_ITEM_VALUE.getQuery()).get();
                int ordinal = dataView
                        .getInt(CustomItemInformation.Queries.ORDINAL)
                        .orElse(0);

                CustomItemInformation info = itemManager.getRegisteredItemInfoForOrdinal(ordinal);
                final CustomItem customItem = info.createNewInstance();
                value.setValue(customItem);

                final DataView blockInfo = (DataView) dataView.get(DataQuery.of("ItemInfo")).get();
                customItem.loadInfo(blockInfo);
            }

            return Optional.of(value);
        }
    }

}