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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(FleeEntityGoal.class)
public class FleeGoalMixin<T extends LivingEntity> {
    @Shadow @Final protected PathAwareEntity mob;
    @Mutable
    @Shadow @Final private TargetPredicate withinRangePredicate;

    // The goal keeps the navigation the mob had when created, not the vehicle one used while riding:
    // the vehicle never reaches the end of the path, and spins there forever
    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void cancelFleeWhileRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.hasVehicle()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At("TAIL"))
    private void changeFleePredicate(PathAwareEntity mob, Class<T> fleeFromType, Predicate<LivingEntity> extraInclusionSelector, float distance,
                                     double slowSpeed, double fastSpeed, Predicate<LivingEntity> inclusionSelector, CallbackInfo ci) {
        this.withinRangePredicate = TargetPredicate.createAttackable()
                .setBaseMaxDistance(distance)
                .setPredicate((entity, world) -> inclusionSelector.test(entity)
                        && extraInclusionSelector.test(entity)
                        && (!ModConfig.get().general.mobsLessFear || entity.getHealth() > entity.getMaxHealth() / 2.f));
    }
}
