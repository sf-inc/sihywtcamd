package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomAttackPlayerTargetGoal")
public class PhantomTargetGoalMixin {
    @Unique
    private static final TargetingConditions NO_VISIBILITY_PREDICATE = TargetingConditions.forCombat().ignoreLineOfSight();

    @WrapOperation(method = "canUse()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getNearbyPlayers(Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private List<Player> changePlayerPredicateStart(ServerLevel instance, TargetingConditions targetPredicate, LivingEntity livingEntity,
                                                          AABB box, Operation<List<Player>> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, targetPredicate.copy().ignoreLineOfSight(), livingEntity, box)
                : original.call(instance, targetPredicate, livingEntity, box);
    }

    @WrapOperation(method = "canUse()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Phantom;canAttack(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;)Z"))
    private boolean changePredicateStart(Phantom instance, ServerLevel world, LivingEntity target,
                                          TargetingConditions predicate, Operation<Boolean> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, world, target, NO_VISIBILITY_PREDICATE)
                : original.call(instance, world, target, predicate);
    }

    @WrapOperation(method = "canContinueToUse()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Phantom;canAttack(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;)Z"))
    private boolean changePredicateContinue(Phantom instance, ServerLevel world, LivingEntity target,
                                          TargetingConditions predicate, Operation<Boolean> original) {
        return ModConfig.get().undead.phantom.throughBlocks
                ? original.call(instance, world, target, NO_VISIBILITY_PREDICATE)
                : original.call(instance, world, target, predicate);
    }
}
