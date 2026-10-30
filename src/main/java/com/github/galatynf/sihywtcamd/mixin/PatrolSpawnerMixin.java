package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {
    @Inject(method = "spawnPatrolMember", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/EntitySpawnReason;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    private void spawnVindicatorOrRavager(ServerLevel world, BlockPos pos, RandomSource random, boolean captain, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().illagers.vindicator.spawnInPatrols
                && random.nextFloat() < 0.1F + 0.1F * world.getCurrentDifficultyAt(pos).getSpecialMultiplier()) {
            PatrollingMonster patrolEntity = EntityTypes.VINDICATOR.create(world, EntitySpawnReason.PATROL);
            if (patrolEntity != null) {
                if (captain) {
                    patrolEntity.setPatrolLeader(true);
                    patrolEntity.findPatrolTarget();
                }

                patrolEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                patrolEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.PATROL, null);
                world.addFreshEntityWithPassengers(patrolEntity);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
            return;
        }

        if (ModConfig.get().illagers.ravager.spawnInPatrols
                && random.nextFloat() < 0.05F + 0.05F * world.getCurrentDifficultyAt(pos).getSpecialMultiplier()
                && world.noCollision(EntityTypes.RAVAGER.getSpawnAABB(pos.getX(), pos.getY(), pos.getZ()))) {
            PatrollingMonster patrolEntity1 = EntityTypes.RAVAGER.create(world, EntitySpawnReason.PATROL);
            PatrollingMonster patrolEntity2 = captain || random.nextBoolean()
                    ? EntityTypes.PILLAGER.create(world, EntitySpawnReason.PATROL) : null;

            if (patrolEntity1 != null) {
                patrolEntity1.setPos(pos.getX(), pos.getY(), pos.getZ());
                patrolEntity1.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.PATROL, null);

                if (patrolEntity2 != null) {
                    patrolEntity2.setPos(pos.getX(), pos.getY(), pos.getZ());
                    if (captain) {
                        patrolEntity2.setPatrolLeader(true);
                        patrolEntity2.findPatrolTarget();
                    }

                    patrolEntity2.startRiding(patrolEntity1);
                    patrolEntity2.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.PATROL, null);
                }

                world.addFreshEntityWithPassengers(patrolEntity1);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
