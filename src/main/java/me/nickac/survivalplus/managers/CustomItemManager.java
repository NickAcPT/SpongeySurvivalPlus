package me.nickac.survivalplus.managers;

import com.google.inject.Singleton;
import me.nickac.survivalplus.custom.items.CustomItemBaseEnum;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;

@Singleton
public class CustomItemManager {
    private String UNSAFE_DATA = "UnsafeData";
    private String MANAGED_ITEM = "ManagedItem";
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
