package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Random;

import static net.minecraft.entity.mob.MobEntity.canMobSpawn;

public class Utils {
    public static boolean canCaveSpiderSpawn(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return false;
        } else if (world.getLightLevel(pos) > 7) {
            return false;
        } else {
            double cbrt = Math.cbrt(pos.getY() + Math.abs(world.getBottomY()));
            return cbrt < 4.0D
                    && random.nextDouble() > cbrt / 4.0D
                    && canMobSpawn(type, world, spawnReason, pos, random);
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

        switch (ModConfig.get().zombie.general.typesBuffed) {
            case NONE -> {
            }
            case ZOMBIE_ONLY -> res = zombieOnly;
            case NO_DROWNED -> res = zombieOnly || isHusk || isPiglin;
            case NO_PIGLIN -> res = zombieOnly || isDrowned || isHusk;
            case ALL -> res = zombieOnly || isDrowned || isHusk || isPiglin;
        }

        return res;
    }

    public static boolean isFireSource(final DamageSource damageSource) {
        return damageSource.equals(DamageSource.ON_FIRE)
                || damageSource.equals(DamageSource.IN_FIRE)
                || damageSource.equals(DamageSource.HOT_FLOOR);
    }
}
