package com.github.galatynf.sihywtcamd.mixin.spider;

import net.minecraft.block.BlockState;
import net.minecraft.block.CobwebBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public class SwordMixin {
    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void canBreakEveryCowebs(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        if (state.getBlock() instanceof CobwebBlock) {
            cir.setReturnValue(15.0F);
        }
    }

    @Inject(method = "isSuitableFor", at = @At("TAIL"), cancellable = true)
    private void canBreakEveryCowebs2(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() || state.getBlock() instanceof CobwebBlock);
    }
}
