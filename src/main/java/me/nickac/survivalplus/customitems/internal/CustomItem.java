package me.nickac.survivalplus.customitems.internal;

import me.nickac.survivalplus.customitems.internal.info.CustomItemInformation;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;

public abstract class CustomItem implements DataSerializable {
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
        return DataContainer.createNew();
    }

    CustomItemInformation getInfo() {
        return info;
    }

    public final void setInfo(CustomItemInformation info) {
        this.info = info;
    }
    //endregion

    public void onInteract(Player player, InteractItemEvent event) {
    }

}
