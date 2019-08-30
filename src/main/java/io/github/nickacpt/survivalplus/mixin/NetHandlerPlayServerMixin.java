package io.github.nickacpt.survivalplus.mixin;

import net.minecraft.inventory.Container;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Container.class})
public class NetHandlerPlayServerMixin {
//    @Shadow public List<Slot> inventorySlots;
//
//    @Inject(method = "slotClick", at = @At("HEAD"), cancellable = true)
//    public void onSlotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player,
//                            CallbackInfoReturnable<ItemStack> cir) {
//        System.out.println("On click");
//
//    }
//
//    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true)
//    public void onTransferStackInSlot(EntityPlayer playerIn, int index, CallbackInfoReturnable<ItemStack> cir) {
//        System.out.println("Transfer Stack");
////        ((Player)playerIn).sendMessage(Text.of("Sup bro").to);
//        cir.setReturnValue(ItemStack.EMPTY);
//    }
}
