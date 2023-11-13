package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpiderEntity.class)
public class CaveSpiderMixin extends SpiderEntity {
    public CaveSpiderMixin(EntityType<? extends SpiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (ModConfig.get().arthropods.spider.caveSpiderJockey
                && !this.hasVehicle()
                && !spawnReason.equals(SpawnReason.SPAWNER)
                && world.getRandom().nextInt(50) == 0) {
            SkeletonEntity skeletonEntity = EntityType.SKELETON.create(this.getWorld());
            if (skeletonEntity != null) {
                skeletonEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                skeletonEntity.initialize(world, difficulty, spawnReason, null, null);
                skeletonEntity.startRiding(this);
            }
        }

        return entityData;
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F;
    }

    @Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
    private void updateCaveSpiderEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.45F * this.getScaleFactor());
    }

    @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean adaptivePoison(LivingEntity target, StatusEffectInstance effect, Entity source) {
        int duration = this.isBaby() ? effect.getDuration() / 5 : effect.getDuration();
        return target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, duration, 0), this);
    }
}
