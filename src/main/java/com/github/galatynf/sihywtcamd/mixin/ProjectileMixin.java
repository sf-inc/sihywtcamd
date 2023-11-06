package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class ProjectileMixin {
    @Inject(method = "onEntityHit", at = @At("HEAD"))
    private void freezeEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (MyComponents.ARROW_COMPONENT.get(this).isFrozen()) {
            Entity entity = entityHitResult.getEntity();
            entity.setFrozenTicks(entity.getFrozenTicks() + entity.getMinFreezeDamageTicks());
        }
    }
}
