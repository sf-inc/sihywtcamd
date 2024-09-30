package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin extends EntityMixin {
    @Override
    protected Vec3d updatePassengerAttachmentPos(Vec3d original, Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        double widthOffset = (dimensions.width() + passenger.getWidth()) / 2;
        double heightOffset = -passenger.getHeight() / 3;
        return original.add(
                Vec3d.fromPolar(0.f, passenger.getYaw())
                        .multiply(widthOffset)
                        .withAxis(Direction.Axis.Y, heightOffset));
    }
}
