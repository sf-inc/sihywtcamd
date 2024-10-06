package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HostileEntity.class)
public class HostileMixin extends PathAwareEntity {
    protected HostileMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "canSpawnInDark", at = @At("RETURN"))
    private static boolean changeCaveSpiderSpawn(boolean original, EntityType<? extends HostileEntity> type,
                                              ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos,
                                              Random random) {
        return original &&
                (!spawnReason.equals(SpawnReason.NATURAL)
                        || !type.equals(EntityType.CAVE_SPIDER)
                        || canCaveSpiderSpawn(world, pos, random));
    }

    @Unique
    private static boolean canCaveSpiderSpawn(WorldAccess world, BlockPos pos, Random random) {
        double relativeHeight = (double) (pos.getY() - world.getBottomY()) / world.getHeight();
        return ModConfig.get().arthropods.caveSpider.naturalSpawn
                && random.nextDouble() < (1.F - 3.F * relativeHeight);
    }
}
