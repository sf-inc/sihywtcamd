package com.github.galatynf.sihywtcamd.mixin.wither;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WitherSkullBlock.class)
public class WitherSkullMixin {
    @ModifyVariable(method = "checkSpawn(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/SkullBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private static WitherBoss initWitherOnSummoned(WitherBoss witherEntity, Level world, BlockPos pos, SkullBlockEntity blockEntity) {
        if (world instanceof ServerLevelAccessor serverWorld) {
            witherEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
        }
        return witherEntity;
    }
}
