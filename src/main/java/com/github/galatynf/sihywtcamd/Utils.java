package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Random;

import static net.minecraft.entity.mob.MobEntity.canMobSpawn;

public class Utils {
    public static boolean canCaveSpiderSpawn(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return false;
        } else if (world.getLightLevel(pos) > 8) {
            return false;
        } else {
            return random.nextInt((int) Math.cbrt(pos.getY())) == 0 && canMobSpawn(type, world, spawnReason, pos, random);
        }
    }

    public static boolean isZombieTypeBuffed(EntityType<?> entityType) {
        boolean zombieOnly = entityType.equals(EntityType.ZOMBIE)
                || entityType.equals(EntityType.ZOMBIE_VILLAGER)
                || entityType.equals(EntityType.ZOMBIE_HORSE)
                || entityType.equals(EntityType.GIANT);
        boolean isDrowned = entityType.equals(EntityType.DROWNED);
        boolean isPiglin = entityType.equals(EntityType.ZOMBIFIED_PIGLIN);
        boolean isHusk = entityType.equals(EntityType.HUSK);
        boolean res = false;

        switch (ModConfig.get().overworld.zombieTypeBuffed) {
            case NONE -> {
            }
            case ZOMBIE_ONLY -> res = zombieOnly;
            case NO_DROWNED -> res = zombieOnly || isHusk || isPiglin;
            case NO_PIGLIN -> res = zombieOnly || isDrowned || isHusk;
            case ALL -> res = zombieOnly || isDrowned || isHusk || isPiglin;
        }

        return res;
    }
}
