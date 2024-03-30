package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;onAttacking(Lnet/minecraft/entity/Entity;)V"))
    protected void onTryAttackSuccess(Entity target, CallbackInfoReturnable<Boolean> cir) {

    }

    @Inject(method = "initialize", at = @At("TAIL"))
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                              EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {

    }

    @Inject(method = "setBaby", at = @At("TAIL"))
    protected void onSetBaby(boolean baby, CallbackInfo ci) {

    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void onInitDataTracker(CallbackInfo ci) {

    }
}
