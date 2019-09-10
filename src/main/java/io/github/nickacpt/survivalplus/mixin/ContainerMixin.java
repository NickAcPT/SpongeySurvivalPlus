package io.github.nickacpt.survivalplus.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Container.class)
public abstract class ContainerMixin {

    @Shadow
    public List<Slot> inventorySlots;

    @Shadow
    public NonNullList<ItemStack> inventoryItemStacks;

    @Shadow
    protected List<IContainerListener> listeners;

    private void sendAllChanges() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack stack = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
            this.inventoryItemStacks.set(i, stack);

            for (IContainerListener listener : this.listeners) {
                listener.sendSlotContents(((Container) (Object) this), i, stack);
            }
        }
    }

    @Inject(method = "slotClick", at = @At("RETURN"))
    public void onSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player,
                            CallbackInfoReturnable<ItemStack> cir) {
        this.sendAllChanges();
    }
}
