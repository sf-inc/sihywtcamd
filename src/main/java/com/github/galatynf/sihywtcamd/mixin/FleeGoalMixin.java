package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(FleeEntityGoal.class)
public class FleeGoalMixin<T extends LivingEntity> {
    @Mutable
    @Shadow @Final private TargetPredicate withinRangePredicate;

    @Inject(method = "<init>(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At("TAIL"))
    private void changeFleePredicate(PathAwareEntity mob, Class<T> fleeFromType, Predicate<LivingEntity> extraInclusionSelector, float distance,
                                     double slowSpeed, double fastSpeed, Predicate<LivingEntity> inclusionSelector, CallbackInfo ci) {
        if (ModConfig.get().general.mobsLessFear) {
            this.withinRangePredicate = (TargetPredicate.createAttackable()).setBaseMaxDistance(distance).setPredicate(
                    inclusionSelector.and(extraInclusionSelector).and(livingEntity -> livingEntity.getHealth() > livingEntity.getMaxHealth() / 2.0F));
        }
    }
}
