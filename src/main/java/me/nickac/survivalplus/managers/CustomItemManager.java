package me.nickac.survivalplus.managers;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.nickac.survivalplus.customitems.internal.CustomBlock;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import me.nickac.survivalplus.data.CustomKeys;
import me.nickac.survivalplus.data.impl.CustomItemData;
import me.nickac.survivalplus.data.impl.CustomItemInfoData;
import me.nickac.survivalplus.model.CustomRawAsset;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("UnstableApiUsage")
@Singleton
public class CustomItemManager {
    private final Map<Integer, CustomItemInformation> registeredItems = new HashMap<>();
    private AtomicInteger ordinalCount = new AtomicInteger();

    @Inject
    private PluginContainer container;

    public void registerItem(CustomItemInformation info) {
        Preconditions.checkNotNull(info.getItemClass());
        Preconditions.checkNotNull(info.getModelAsset());
        registeredItems.put(info.getOrdinal(), info);

        if (info.isDirectional()) {
            final String name = Files.getNameWithoutExtension(info.getModelAssetRaw());
            final String ext = Files.getFileExtension(info.getModelAssetRaw());

            Arrays.stream(Direction.values())
                    .filter(Direction::isCardinal)
                    .filter(c -> !c.isSecondaryOrdinal())
                    .filter(c -> !c.isUpright())
                    .forEach(dir -> {
                        final char side = dir.name().toLowerCase().charAt(0);

                        final CustomItemInformation build = CustomItemInformation.builder()
                                .named(info.getName())
                                .ownedBy(info.getItemClass())
                                .internal()
                                .withModel(String.format("%s-side-%s.%s", name, side, ext))
                                .build();

                        try {
                            String customModel = info.getModelAsset().readString();
                            customModel = customModel.replace("block/custom_block",
                                    "block/custom_block-" + side);

                            build.setCustomAsset(new CustomRawAsset(container,
                                    customModel.getBytes(StandardCharsets.UTF_8)));
                            registeredItems.put(build.getOrdinal(), build);

                            info.addDirectionalInfo(dir, build);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public Optional<CustomItemInformation> findInfoForAsset(String name) {
        return registeredItems.values().stream().filter(i -> i.getModelAssetRaw().equals(name)).findFirst();
    }

    public int getNextOrdinal() {
        return ordinalCount.incrementAndGet();
    }

    public boolean isManagedItem(ItemStackSnapshot item) {
        return item.get(CustomItemInfoData.Immutable.class).isPresent();
    }

    public boolean isManagedBlockItem(ItemStackSnapshot item) {
        return isManagedItem(item) && getCustomItemDataFromItem(item).isBlock();
    }

    public boolean isManagedBlock(BlockSnapshot block) {
        return block.get(CustomItemData.Immutable.class).map(i -> !((CustomBlock)i.getValueGetter().get()).isMarkedForRemoval()).orElse(false);
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
