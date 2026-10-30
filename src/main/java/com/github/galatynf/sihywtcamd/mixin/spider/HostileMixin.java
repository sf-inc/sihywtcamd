package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Monster.class)
public class HostileMixin extends PathfinderMob {
    protected HostileMixin(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "checkMonsterSpawnRules", at = @At("RETURN"))
    private static boolean changeCaveSpiderSpawn(boolean original, EntityType<? extends Monster> type,
                                              ServerLevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos,
                                              RandomSource random) {
        return original &&
                (!spawnReason.equals(EntitySpawnReason.NATURAL)
                        || !type.equals(EntityType.CAVE_SPIDER)
                        || canCaveSpiderSpawn(world, pos, random));
    }

    @Unique
    private static boolean canCaveSpiderSpawn(LevelAccessor world, BlockPos pos, RandomSource random) {
        double relativeHeight = (double) (pos.getY() - world.getMinY()) / world.getHeight();
        return ModConfig.get().arthropods.caveSpider.naturalSpawn
                && random.nextDouble() < (1.F - 3.F * relativeHeight);
    }
}
