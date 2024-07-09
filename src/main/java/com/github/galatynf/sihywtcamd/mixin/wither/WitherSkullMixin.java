package com.github.galatynf.sihywtcamd.mixin.wither;

import net.minecraft.block.WitherSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WitherSkullBlock.class)
public class WitherSkullMixin {
    @ModifyVariable(method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static WitherEntity initWitherOnSummoned(WitherEntity witherEntity, World world, BlockPos pos, SkullBlockEntity blockEntity) {
        witherEntity.initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.MOB_SUMMONED, null);
        return witherEntity;
    }
}
