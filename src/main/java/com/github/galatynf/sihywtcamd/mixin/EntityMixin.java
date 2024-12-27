package com.github.galatynf.sihywtcamd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract float distanceTo(Entity entity);
    @Shadow public abstract Box getBoundingBox();
    @Shadow public abstract World getWorld();

    @ModifyReturnValue(method = "isAlwaysInvulnerableTo", at = @At("RETURN"))
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return original;
    }

    @ModifyReturnValue(method = "isImmuneToExplosion", at = @At("RETURN"))
    protected boolean updateImmunityToExplosion(boolean original, Explosion explosion) {
        return original;
    }

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    protected boolean updateImmunityToFire(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "getPassengerAttachmentPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityDimensions;F)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"))
    protected Vec3d updatePassengerAttachmentPos(Vec3d original, Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return original;
    }
}
