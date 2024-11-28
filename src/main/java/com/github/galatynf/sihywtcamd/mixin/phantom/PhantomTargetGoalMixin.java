package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$FindTargetGoal")
public class PhantomTargetGoalMixin {
    @WrapOperation(method = "canStart()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getPlayers(Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private List<PlayerEntity> changePlayerPredicateStart(ServerWorld instance, TargetPredicate targetPredicate, LivingEntity livingEntity,
                                                          Box box, Operation<List<PlayerEntity>> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, targetPredicate.ignoreVisibility(), livingEntity, box)
                : original.call(instance, targetPredicate, livingEntity, box);
    }

    @WrapOperation(method = "canStart()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;testTargetPredicate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private boolean changePredicateStart(PhantomEntity instance, ServerWorld world, LivingEntity target,
                                          TargetPredicate predicate, Operation<Boolean> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, world, target, predicate.ignoreVisibility())
                : original.call(instance, world, target, predicate);
    }

    @WrapOperation(method = "canStart()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;testTargetPredicate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private boolean changePredicateContinue(PhantomEntity instance, ServerWorld world, LivingEntity target,
                                          TargetPredicate predicate, Operation<Boolean> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, world, target, predicate.ignoreVisibility())
                : original.call(instance, world, target, predicate);
    }
}
