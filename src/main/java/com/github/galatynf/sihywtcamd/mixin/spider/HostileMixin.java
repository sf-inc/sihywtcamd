package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.entity.mob.MobEntity.canMobSpawn;

@Mixin(HostileEntity.class)
public class HostileMixin {
    @Inject(method = "canSpawnInDark", at = @At("HEAD"), cancellable = true)
    private static void changeCaveSpiderSpawn(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason,
                                              BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().arthropods.spider.caveSpiderNaturalSpawn
                && type.equals(EntityType.CAVE_SPIDER)
                && !spawnReason.equals(SpawnReason.SPAWNER)) {
            cir.setReturnValue(canCaveSpiderSpawn(type, world, spawnReason, pos, random));
        }
    }

    @Unique
    private static boolean canCaveSpiderSpawn(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        double relativeHeight = (double) (pos.getY() - world.getBottomY()) / world.getHeight();
        return random.nextDouble() < (1.F - 3.F * relativeHeight)
                && canMobSpawn(type, world, spawnReason, pos, random);
    }
}
