package me.nickac.survivalplus.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import me.nickac.survivalplus.custom.items.CustomItemBaseEnum;
import me.nickac.survivalplus.data.ManagedTypeData;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
@Singleton
public class CustomItemManager {
    private String MANAGED_ITEM = "ManagedItem";
    private String UNSAFE_DATA = "UnsafeData";
    private DataQuery ManagedItemDataQuery = DataQuery.of(UNSAFE_DATA, MANAGED_ITEM);

    public boolean isManagedItem(DataContainer container) {
        return container.get(ManagedItemDataQuery).isPresent();
    }

    public ItemStack generateCustomItemStack(int itemCount) {
        CustomItemBaseEnum countedItem = CustomItemBaseEnum.getForCountedItem(itemCount);

        DataContainer dataContainer = ItemStack.builder()
                .itemType(countedItem.getItemType())
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.ITEM_DURABILITY, countedItem.getMaxDamage() - (itemCount % countedItem.getMaxDamage()))
                .build().toContainer();

        dataContainer.set(ManagedItemDataQuery, true);

        return ItemStack.builder().fromContainer(dataContainer).build();

    }

}
