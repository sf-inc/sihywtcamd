package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.monster.Phantom;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public abstract class PhantomSwoopGoalMixin {
    @Final @Shadow Phantom field_7333;

    @Inject(method = "canContinueToUse()Z", at = @At("HEAD"), cancellable = true)
    private void fearLight(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().undead.phantom.lightFear
                && this.field_7333.level().getMaxLocalRawBrightness(this.field_7333.blockPosition(), 15) > 10) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "canContinueToUse()Z", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean removeCatFear(List<Cat> list, Operation<Boolean> original) {
        if (ModConfig.get().general.mobsLessFear) {
            for (Cat catEntity : list) {
                if (catEntity.getHealth() > catEntity.getMaxHealth() / 2.0F) {
                    return false;
                }
            }
            return true;
        }
        return original.call(list);
    }
}
