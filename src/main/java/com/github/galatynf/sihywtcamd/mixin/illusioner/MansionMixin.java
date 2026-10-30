package com.github.galatynf.sihywtcamd.mixin.illusioner;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;

@Mixin(WoodlandMansionPieces.WoodlandMansionPiece.class)
public class MansionMixin {
    @Inject(method = "handleDataMarker", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void trySetIllusioner(String metadata, BlockPos pos, ServerLevelAccessor world, RandomSource random, BoundingBox boundingBox, CallbackInfo ci, @Local List<Mob> list) {
        if (ModConfig.get().illagers.illusioner.spawnInMansions
                && metadata.equals("Mage")
                && random.nextFloat() < 0.4f) {
            Mob illusioner = EntityType.ILLUSIONER.create(world.getLevel(), EntitySpawnReason.STRUCTURE);
            if (illusioner != null) {
                if (random.nextFloat() < 0.8f) {
                    list.clear();
                }
                list.add(illusioner);
            }
        }
    }
}
