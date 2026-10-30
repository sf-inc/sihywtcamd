package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HurtByTargetGoal.class)
public abstract class RevengeGoalMixin extends TargetGoal {
    public RevengeGoalMixin(Mob mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void ignoreUndeadAttacks(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().undead.general.ignoreUndeadAttacks
                && this.mob.getType().is(EntityTypeTags.UNDEAD)
                && this.mob.getLastHurtByMob() != null
                && this.mob.getLastHurtByMob().getType().is(EntityTypeTags.UNDEAD)) {
            cir.setReturnValue(false);
        }
    }
}
