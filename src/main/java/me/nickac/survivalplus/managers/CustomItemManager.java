package me.nickac.survivalplus.managers;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.data.impl.CustomItemData;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class CustomItemManager {
    private final Map<Integer, CustomItemInformation> registeredItems = new HashMap<>();

    public void registerItem(CustomItemInformation info) {
        Preconditions.checkNotNull(info.getItemClass());
        Preconditions.checkNotNull(info.getModelAsset());
        registeredItems.put(info.getOrdinal(), info);
    }

    public boolean isManagedItem(ItemStackSnapshot item) {
        return item.get(CustomItemInfoData.Immutable.class).isPresent();
    }

    public boolean isManagedBlockItem(ItemStackSnapshot item) {
        return isManagedItem(item) && getCustomItemDataFromItem(item).isBlock();
    }

    public boolean isManagedBlock(BlockSnapshot block) {
        return block.get(CustomItemData.Immutable.class).isPresent();
    }

    public CustomBlock getManagedBlockInfo(BlockSnapshot block) {
        return (CustomBlock) block.get(CustomItemData.Immutable.class).get().getValueGetter().get();
    }

    public CustomItemInformation getCustomItemDataFromItem(ItemStackSnapshot item) {
        return item.get(CustomItemInfoData.Immutable.class).get().getValueGetter().get();
    }

    public ItemStack generateCustomItemStack(int itemCount) {

        CustomItemBaseEnum countedItem = CustomItemBaseEnum.getForCountedItem(itemCount);
        CustomItemInformation info = getRegisteredItemInfoForOrdinal(itemCount);

        DataContainer dataContainer = ItemStack.builder()
                .itemType(countedItem.getItemType())
                .add(Keys.UNBREAKABLE, true)
                .add(Keys.DISPLAY_NAME, Text.of(TextColors.RESET, info.getName()))
                .add(Keys.HIDE_UNBREAKABLE, true)
                .add(Keys.ITEM_DURABILITY, countedItem.getMaxDamage() - (itemCount % countedItem.getMaxDamage()))
                .build().toContainer();

        ItemStack stack = ItemStack.builder().fromContainer(dataContainer).build();
        stack.offer(stack.getOrCreate(CustomItemInfoData.class).get());

        stack.tryOffer(CustomKeys.CUSTOM_ITEM_INFORMATION_VALUE, info);

        return stack;

    }

    public Collection<CustomItemInformation> getRegisteredItems() {
        return registeredItems.values();
    }

    public CustomItemInformation getRegisteredItemInfoForOrdinal(int itemCount) {
        if (!registeredItems.containsKey(itemCount))
            throw new RuntimeException("Unregistered item number #" + itemCount);

        return registeredItems.get(itemCount);
    }

}
