package com.github.galatynf.sihywtcamd.mixin.illusioner;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.structure.WoodlandMansionGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

@Mixin(WoodlandMansionGenerator.Piece.class)
public class MansionMixin {
    @ModifyVariable(method = "handleMetadata", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/IllagerEntity;setPersistent()V"))
    private IllagerEntity trySetIllusioner(IllagerEntity illager, String metadata, BlockPos pos, ServerWorldAccess world,
                                           Random random, BlockBox boundingBox) {
        if (ModConfig.get().overworld.illagers.illusionerInMansions
                && metadata.equals("Mage")
                && random.nextFloat() < 0.4f) {
            if (random.nextFloat() < 0.2f) {
                System.out.println("+++");
                IllagerEntity illager2 = EntityType.ILLUSIONER.create(world.toServerWorld());
                illager2.setPersistent();
                illager2.refreshPositionAndAngles(pos, 0.0f, 0.0f);
                illager2.initialize(world, world.getLocalDifficulty(illager2.getBlockPos()), SpawnReason.STRUCTURE, null, null);
                world.spawnEntityAndPassengers(illager2);
            } else {
                illager = EntityType.ILLUSIONER.create(world.toServerWorld());
                illager.setPersistent();
            }
        }
        return illager;
    }
}
