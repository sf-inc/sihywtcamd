package com.github.galatynf.sihywtcamd.mixin.illusioner;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.structure.WoodlandMansionGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WoodlandMansionGenerator.Piece.class)
public class MansionMixin {
    @Inject(method = "handleMetadata", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void trySetIllusioner(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci, @Local List<MobEntity> list) {
        if (ModConfig.get().illagers.illusioner.spawnInMansions
                && metadata.equals("Mage")
                && random.nextFloat() < 0.4f) {
            MobEntity illusioner = EntityType.ILLUSIONER.create(world.toServerWorld(), SpawnReason.STRUCTURE);
            if (illusioner != null) {
                if (random.nextFloat() < 0.8f) {
                    list.clear();
                }
                list.add(illusioner);
            }
        }
    }
}
