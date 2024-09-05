package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveSpiderEntity.class)
public class CaveSpiderMixin extends SpiderEntity {
    public CaveSpiderMixin(EntityType<? extends SpiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void spawnBaby(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                  EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().arthropods.caveSpider.babySpawnGroup
                && !this.isBaby()
                && !this.hasPassengers()
                && !SpawnReason.isAnySpawner(spawnReason)
                && this.random.nextFloat() < 0.15F) {
            this.setBaby(true);

            for (int i = 0; i < 2; ++i) {
                MobEntity mob = (MobEntity) this.getType().spawn((ServerWorld) this.getWorld(), this.getBlockPos(), SpawnReason.NATURAL);
                if (mob != null) {
                    mob.setBaby(true);
                }
            }
        }
    }
}
