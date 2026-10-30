package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class ProjectileMixin {
    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;)V"))
    private void freezeEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (MyComponents.ARROW_COMPONENT.get(this).isFrozen()) {
            Entity entity = entityHitResult.getEntity();
            int maxFrozenTicks = 2 * entity.getTicksRequiredToFreeze();
            if (entity.canFreeze() && entity.getTicksFrozen() < maxFrozenTicks) {
                entity.setTicksFrozen(Math.min(entity.getTicksFrozen() + entity.getTicksRequiredToFreeze(), maxFrozenTicks));
            }
        }
    }
}
