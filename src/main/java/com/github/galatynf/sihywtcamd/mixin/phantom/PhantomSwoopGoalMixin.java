package com.github.galatynf.sihywtcamd.mixin.phantom;

import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal")
public class PhantomSwoopGoalMixin {
    @Shadow private PhantomEntity field_7333;
    @Inject(method = "shouldContinue", at = @At("TAIL"), cancellable = true)
    private void changePredicateContinue(CallbackInfoReturnable<Boolean> cir) {
        if (field_7333.world.getLightLevel(field_7333.getBlockPos()) > 7) {
            cir.setReturnValue(false);
        }
    }
}
