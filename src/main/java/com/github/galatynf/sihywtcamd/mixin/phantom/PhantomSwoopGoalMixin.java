package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.passive.CatEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$SwoopMovementGoal")
public abstract class PhantomSwoopGoalMixin {
    @Final @Shadow PhantomEntity field_7333;

    @Inject(method = "shouldContinue()Z", at = @At("HEAD"), cancellable = true)
    private void fearLight(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().undead.phantom.lightFear
                && this.field_7333.getEntityWorld().getLightLevel(this.field_7333.getBlockPos(), 15) > 10) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "shouldContinue()Z", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"))
    private boolean removeCatFear(List<CatEntity> list, Operation<Boolean> original) {
        if (ModConfig.get().general.mobsLessFear) {
            for (CatEntity catEntity : list) {
                if (catEntity.getHealth() > catEntity.getMaxHealth() / 2.0F) {
                    return false;
                }
            }
            return true;
        }
        return original.call(list);
    }
}
