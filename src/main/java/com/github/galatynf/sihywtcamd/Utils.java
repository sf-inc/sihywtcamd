package com.github.galatynf.sihywtcamd;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

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
}
