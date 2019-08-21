package me.nickac.survivalplus.managers;

import com.google.inject.Singleton;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.customitems.internal.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class CustomItemManager {
    private Map<Integer, CustomItemInformation> registeredItems = new HashMap<>();

    public void registerItem(CustomItemInformation info) {
        registeredItems.put(info.getOrdinal(), info);
    }

    public boolean isManagedItem(ItemStackSnapshot item) {
        return item.get(CustomItemInfoData.Immutable.class).isPresent();
    }

    public CustomItemInformation getCustomItemDataFromItem(ItemStackSnapshot item) {
        return item.get(CustomItemInfoData.Immutable.class).get().getValueGetter().get();
    }

    public ItemStack generateCustomItemStack(int itemCount) {

        CustomItemBaseEnum countedItem = CustomItemBaseEnum.getForCountedItem(itemCount);
        CustomItemInformation info = getInformationForCount(itemCount);

        DataContainer dataContainer = ItemStack.builder()
                .itemType(countedItem.getItemType())
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.DISPLAY_NAME, Text.of(TextColors.RESET, info.getName()))
                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.ITEM_DURABILITY, countedItem.getMaxDamage() - (itemCount % countedItem.getMaxDamage()))
                .build().toContainer();

        ItemStack stack = ItemStack.builder().fromContainer(dataContainer).build();
        stack.offer(stack.getOrCreate(CustomItemInfoData.class).get());

        DataTransactionResult result = stack.tryOffer(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE,
                info);

        return stack;

    }

    private CustomItemInformation getInformationForCount(int itemCount) {
        if (!registeredItems.containsKey(itemCount))
            throw new RuntimeException("Unregistered item number #" + itemCount);

        return registeredItems.get(itemCount);
    }

}
