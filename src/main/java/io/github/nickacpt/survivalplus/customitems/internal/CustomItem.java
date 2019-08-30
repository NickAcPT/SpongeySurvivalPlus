package io.github.nickacpt.survivalplus.customitems.internal;

import com.google.inject.Inject;
import io.github.nickacpt.survivalplus.customitems.internal.info.CustomItemInformation;
import io.github.nickacpt.survivalplus.managers.CustomItemManager;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;

/**
 * Represents a custom item created by SurvivalPlus
 */
public abstract class CustomItem implements DataSerializable {
    @Inject
    protected static CustomItemManager itemManager;
    //region Internal
    public static CustomItem EMPTY = new CustomItem() {
    };
    private CustomItemInformation info;


    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer finalContainer = getInfo() != null ? getInfo().toContainer() : DataContainer.createNew();
        final DataContainer info = DataContainer.createNew();
        saveInfo(info);
        return finalContainer.set(DataQuery.of("ItemInfo"), info);
    }

    public CustomItemInformation getInfo() {
        return info;
    }

    public void setInfo(CustomItemInformation info) {
        this.info = info;
    }
    //endregion

    /**
     * Called when a player interacts with this item
     * @param player The player
     * @param event The event representing the interaction
     */
    public void onInteract(Player player, InteractItemEvent event) {
    }
    /**
     * Called when a player interacts with this item on a block
     * @param player The player
     * @param event The event representing the interaction
     */
    public void onInteract(Player player, InteractBlockEvent event) {
    }

    /**
     * Save the information stored by this item.
     * <p>
     * Used to persist the data stored by this item into NBT for Sponge to save.
     * @param view The container to save to.
     */
    public void saveInfo(DataView view) {
    }

    /**
     * Load the information stored by this item.
     * <p>
     * Used to get the persisted data by this item from NBT.
     * @param view The container to load from.
     */
    public void loadInfo(DataView view) {
    }

}
