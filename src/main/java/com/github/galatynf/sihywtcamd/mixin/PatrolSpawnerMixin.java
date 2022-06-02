package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {
    @Inject(method = "spawnPillager", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"), cancellable = true)
    private void spawnVindicatorOrRavager(ServerWorld world, BlockPos pos, Random random, boolean captain, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().illager.vindicator.spawnInPatrols
                && random.nextFloat() < 0.1F + 0.1F * world.getLocalDifficulty(pos).getClampedLocalDifficulty()) {
            PatrolEntity patrolEntity = EntityType.VINDICATOR.create(world);
            if (patrolEntity != null) {
                if (captain) {
                    patrolEntity.setPatrolLeader(true);
                    patrolEntity.setRandomPatrolTarget();
                }

                patrolEntity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
                world.spawnEntityAndPassengers(patrolEntity);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
            return;
        }

        if (ModConfig.get().illager.ravagerInPatrols
                && random.nextFloat() < 0.05F + 0.05F * world.getLocalDifficulty(pos).getClampedLocalDifficulty()) {
            PatrolEntity patrolEntity1 = EntityType.RAVAGER.create(world);
            PatrolEntity patrolEntity2 = captain || random.nextBoolean() ? EntityType.PILLAGER.create(world) : null;

            if (patrolEntity1 != null) {
                if (patrolEntity2 != null) {
                    patrolEntity2.startRiding(patrolEntity1);
                    if (captain) {
                        patrolEntity2.setPatrolLeader(true);
                        patrolEntity2.setRandomPatrolTarget();
                    }
                }

                patrolEntity1.setPosition(pos.getX(), pos.getY(), pos.getZ());
                patrolEntity1.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, null, null);
                world.spawnEntityAndPassengers(patrolEntity1);
                cir.setReturnValue(true);
            } else {
                cir.setReturnValue(false);
            }
        }
    }
}
