package io.github.nickacpt.survivalplus.mixin;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin implements org.spongepowered.api.item.inventory.Slot {

    @Shadow @Final public IInventory inventory;

    @Inject(method = "isItemValid", at = @At("HEAD"), cancellable = true)
    public void reee(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        final int pos = getProperty(SlotIndex.class, "slotindex").get().getValue();
        if (pos % 2 == 0) {
            cir.setReturnValue(false);
        }
    }

}
