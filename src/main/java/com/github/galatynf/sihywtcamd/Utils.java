package com.github.galatynf.sihywtcamd;

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
}
