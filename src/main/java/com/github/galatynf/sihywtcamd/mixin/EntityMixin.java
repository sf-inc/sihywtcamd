package com.github.galatynf.sihywtcamd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyReturnValue(method = "isAlwaysInvulnerableTo", at = @At("RETURN"))
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return original;
    }

    @ModifyReturnValue(method = "getPassengerAttachmentPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityDimensions;F)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"))
    protected Vec3d updatePassengerAttachmentPos(Vec3d original, Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return original;
    }
}
