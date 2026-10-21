package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$StartAttackGoal")
public class PhantomAttackGoalMixin {
    @Unique
    private static final TargetPredicate NO_VISIBILITY_PREDICATE = TargetPredicate.createAttackable().ignoreVisibility();

    @WrapOperation(method = "canStart()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;testTargetPredicate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private boolean changePredicateStart2(PhantomEntity instance, ServerWorld world, LivingEntity target,
                                          TargetPredicate predicate, Operation<Boolean> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, world, target, NO_VISIBILITY_PREDICATE)
                : original.call(instance, world, target, predicate);
    }
}
