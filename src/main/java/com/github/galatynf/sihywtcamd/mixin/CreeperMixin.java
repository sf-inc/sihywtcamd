package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CreeperEntity.class)
public abstract class CreeperMixin extends HostileEntity {
    @Shadow private int explosionRadius;

    @Shadow public abstract boolean shouldRenderOverlay();

    protected CreeperMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "explode", at = @At("HEAD"))
    private void effectToEntities(CallbackInfo ci) {
        final int explosionRadius = (int) (this.explosionRadius * (this.shouldRenderOverlay() ? 3 : 1.5f));
        final int effectTime = 100;
        Vec3d explosionRadiuses = new Vec3d(explosionRadius, explosionRadius, explosionRadius);
        List<Entity> entityList = this.world.getOtherEntities(this,
                new Box(this.getPos().subtract(explosionRadiuses), this.getPos().add(explosionRadiuses)),
                entity -> entity instanceof LivingEntity && this.distanceTo(entity) < explosionRadius);

        for (Entity entity : entityList) {
            final float coef = 1.0F - (this.distanceTo(entity) / explosionRadius);
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, (int) (effectTime * coef)), this);
        }
    }
}
