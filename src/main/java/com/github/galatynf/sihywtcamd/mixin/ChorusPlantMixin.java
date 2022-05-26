package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ChorusPlantBlock.class)
public class ChorusPlantMixin {
    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;breakBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private void trySpawnEndermite(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (ModConfig.get().end.endermiteInChorus && random.nextFloat() < 0.1f) {
            EntityType.ENDERMITE.spawn(world, null, null, null, pos, SpawnReason.TRIGGERED, false, false);
        }
    }
}
