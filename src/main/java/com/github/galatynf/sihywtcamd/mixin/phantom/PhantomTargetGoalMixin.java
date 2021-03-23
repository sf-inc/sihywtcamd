package com.github.galatynf.sihywtcamd.mixin.phantom;

import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$FindTargetGoal")
public class PhantomTargetGoalMixin {
    @ModifyArg(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getPlayers(Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
    private TargetPredicate changeBasePredicateStart(TargetPredicate predicate) {
        return predicate.includeHidden();
    }

    @ModifyArg(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;isTarget(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private TargetPredicate changePredicateStart(TargetPredicate predicate) {
        return predicate.includeHidden();
    }

    @ModifyArg(method = "shouldContinue", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;isTarget(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private TargetPredicate changePredicateContinue(TargetPredicate predicate) {
        return predicate.includeHidden();
    }
}
