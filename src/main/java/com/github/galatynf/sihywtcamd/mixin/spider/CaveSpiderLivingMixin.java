package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpiderEntity.class)
public abstract class CaveSpiderLivingMixin extends LivingEntityMixin {
    public CaveSpiderLivingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void caveSpiderJockey(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                  EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().arthropods.caveSpider.jockey
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
    }

    @Override
    public float updateScaleFactor(float original) {
        if (ModConfig.get().arthropods.caveSpider.baby && this.isBaby()) {
            return 0.5F;
        } else {
            return original;
        }
    }

    @ModifyReturnValue(method = "getActiveEyeHeight", at = @At("RETURN"))
    private float updateCaveSpiderEyeHeight(float original) {
        if (ModConfig.get().arthropods.caveSpider.baby) {
            return original * this.getScaleFactor();
        } else {
            return original;
        }
    }

    @ModifyExpressionValue(method = "tryAttack", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/effect/StatusEffect;II)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
    private StatusEffectInstance adaptivePoison(StatusEffectInstance original) {
        if (ModConfig.get().arthropods.caveSpider.baby && this.isBaby()) {
            int duration = original.getDuration() / 5;
            return new StatusEffectInstance(original.getEffectType(), duration, original.getAmplifier());
        } else {
            return original;
        }
    }
}
