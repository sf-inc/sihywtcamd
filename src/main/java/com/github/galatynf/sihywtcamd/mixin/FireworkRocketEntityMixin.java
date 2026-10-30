package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends EntityMixin {
    @Override
    protected Vec3 updatePassengerAttachmentPos(Vec3 original, Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        double widthOffset = (dimensions.width() + passenger.getBbWidth()) / 2;
        double heightOffset = -passenger.getBbHeight() / 3;
        return original.add(
                Vec3.directionFromRotation(0.f, passenger.getYRot())
                        .scale(widthOffset)
                        .with(Direction.Axis.Y, heightOffset));
    }
}
