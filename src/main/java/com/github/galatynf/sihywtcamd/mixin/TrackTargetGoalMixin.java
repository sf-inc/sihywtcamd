package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin {
    @Unique
    private static final int MAX_FOLLOW_RANGE = 48;

    @ModifyExpressionValue(method = "shouldContinue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/TrackTargetGoal;getFollowRange()D"))
    private double doubleFollowRange(double original) {
        return ModConfig.get().general.increasedFollowRange
                ? Math.max(original, Math.min(original * 2, MAX_FOLLOW_RANGE))
                : original;
    }
}
