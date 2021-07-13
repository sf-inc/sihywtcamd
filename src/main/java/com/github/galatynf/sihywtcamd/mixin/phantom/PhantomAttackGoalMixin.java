package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.entity.mob.PhantomEntity$StartAttackGoal")
public class PhantomAttackGoalMixin {
    @ModifyArg(method = "Lnet/minecraft/entity/mob/PhantomEntity$StartAttackGoal;canStart()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/PhantomEntity;isTarget(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z"))
    private TargetPredicate changePredicateStart2(TargetPredicate predicate) {
        return ModConfig.get().overworld.phantomThroughBlocks ? predicate.ignoreVisibility() : predicate;
    }
}
