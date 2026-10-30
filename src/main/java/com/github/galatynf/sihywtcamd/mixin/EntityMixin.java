package com.github.galatynf.sihywtcamd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract float distanceTo(Entity entity);
    @Shadow public abstract AABB getBoundingBox();
    @Shadow public abstract Level level();

    @ModifyReturnValue(method = "isInvulnerableToBase", at = @At("RETURN"))
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return original;
    }

    @ModifyReturnValue(method = "fireImmune", at = @At("RETURN"))
    protected boolean updateImmunityToFire(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "getPassengerAttachmentPoint(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EntityDimensions;F)Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"))
    protected Vec3 updatePassengerAttachmentPos(Vec3 original, Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return original;
    }
}
