package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.Utils;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(HostileEntity.class)
public class HostileMixin {
    @Inject(method = "canSpawnInDark", at = @At("HEAD"), cancellable = true)
    private static void changeCaveSpiderSpawn(EntityType<? extends HostileEntity> type, ServerWorldAccess world, SpawnReason spawnReason,
                                              BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().arthropod.spider.caveSpiderNaturalSpawn
                && type.equals(EntityType.CAVE_SPIDER)
                && !spawnReason.equals(SpawnReason.SPAWNER)) {
            cir.setReturnValue(Utils.canCaveSpiderSpawn(type, world, spawnReason, pos, random));
        }
    }
}
