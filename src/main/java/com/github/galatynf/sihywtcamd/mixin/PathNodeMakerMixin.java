package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathNodeMaker.class)
public class PathNodeMakerMixin {
    @Shadow protected int entityBlockXSize;
    @Shadow protected int entityBlockYSize;
    @Shadow protected int entityBlockZSize;

    @Inject(method = "init", at = @At("TAIL"))
    private void updateMobSize(ChunkCache cachedWorld, MobEntity entity, CallbackInfo ci) {
        if (!ModConfig.get().general.pathPassengerAware || !entity.hasPassengers()) return;

        Entity rider = entity;
        float height = 0.0f;
        float maxWidth = entity.getWidth();
        while (rider.hasPassengers()) {
            height += (float) rider.getMountedHeightOffset();
            rider = rider.getFirstPassenger();
            height += (float) rider.getHeightOffset();
            maxWidth = Math.max(maxWidth, rider.getWidth());
        }

        this.entityBlockXSize = MathHelper.ceil(maxWidth);
        this.entityBlockYSize = MathHelper.ceil(height + rider.getHeight());
        this.entityBlockZSize = this.entityBlockXSize;
    }
}
