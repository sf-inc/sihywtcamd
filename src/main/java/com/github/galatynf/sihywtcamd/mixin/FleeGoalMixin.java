package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

@Mixin(AvoidEntityGoal.class)
public class FleeGoalMixin<T extends LivingEntity> {
    @Shadow @Final protected PathfinderMob mob;
    @Mutable
    @Shadow @Final private TargetingConditions avoidEntityTargeting;

    // The goal keeps the navigation the mob had when created, not the vehicle one used while riding:
    // the vehicle never reaches the end of the path, and spins there forever
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void cancelFleeWhileRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.isPassenger()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;Ljava/util/function/Predicate;FDDLjava/util/function/Predicate;)V", at = @At("TAIL"))
    private void changeFleePredicate(PathfinderMob mob, Class<T> fleeFromType, Predicate<LivingEntity> extraInclusionSelector, float distance,
                                     double slowSpeed, double fastSpeed, Predicate<LivingEntity> inclusionSelector, CallbackInfo ci) {
        this.avoidEntityTargeting = TargetingConditions.forCombat()
                .range(distance)
                .selector((entity, world) -> inclusionSelector.test(entity)
                        && extraInclusionSelector.test(entity)
                        && (!ModConfig.get().general.mobsLessFear || entity.getHealth() > entity.getMaxHealth() / 2.f));
    }
}
