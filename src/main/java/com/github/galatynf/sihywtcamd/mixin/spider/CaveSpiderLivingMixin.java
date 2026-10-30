package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.LivingEntityMixin;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpider.class)
public abstract class CaveSpiderLivingMixin extends LivingEntityMixin {
    public CaveSpiderLivingMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void caveSpiderJockey(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                  SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().arthropods.caveSpider.jockey
                && !this.isPassenger()
                && !EntitySpawnReason.isSpawner(spawnReason)
                && world.getRandom().nextInt(50) == 0) {
            Skeleton skeletonEntity = EntityTypes.SKELETON.create(this.level(), EntitySpawnReason.JOCKEY);
            if (skeletonEntity != null) {
                skeletonEntity.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                skeletonEntity.finalizeSpawn(world, difficulty, spawnReason, null);
                skeletonEntity.startRiding(this, false, false);
            }
        }
    }
}
