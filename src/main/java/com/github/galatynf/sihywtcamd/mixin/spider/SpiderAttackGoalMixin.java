package com.github.galatynf.sihywtcamd.mixin.spider;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.entity.mob.SpiderEntity$AttackGoal")
public class SpiderAttackGoalMixin extends MeleeAttackGoal {
    public SpiderAttackGoalMixin(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    @Inject(method = "getSquaredMaxAttackDistance", at = @At("HEAD"), cancellable = true)
    private void adaptiveAttackDistance(LivingEntity entity, CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(4.0 * this.mob.getScaleFactor() + entity.getWidth());
    }
}
