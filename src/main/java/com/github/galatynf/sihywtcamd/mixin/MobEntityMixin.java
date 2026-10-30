package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow @Final protected GoalSelector targetSelector;
    @Shadow @Final protected GoalSelector goalSelector;

    @Shadow public abstract void setPathfindingMalus(PathType nodeType, float penalty);
    @Shadow public abstract LivingEntity getTarget();

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V"))
    protected void onTryAttackSuccess(ServerLevel world, Entity target, CallbackInfoReturnable<Boolean> cir) {

    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    protected void onTickMovement(CallbackInfo ci) {

    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    protected void onInitialize(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {

    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void readModData(ValueInput view, CallbackInfo ci) {

    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void writeModData(ValueOutput view, CallbackInfo ci) {

    }
}
