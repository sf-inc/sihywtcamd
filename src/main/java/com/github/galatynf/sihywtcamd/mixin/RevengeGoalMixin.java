package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RevengeGoal.class)
public abstract class RevengeGoalMixin extends TrackTargetGoal {
    public RevengeGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void ignoreUndeadAttacks(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().undead.general.ignoreUndeadAttacks
                && this.mob.isUndead()
                && this.mob.getAttacker() != null
                && this.mob.getAttacker().isUndead()) {
            cir.setReturnValue(false);
        }
    }
}
