package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NodeEvaluator.class)
public class PathNodeMakerMixin {
    @Shadow protected int entityWidth;
    @Shadow protected int entityHeight;
    @Shadow protected int entityDepth;

    @Inject(method = "prepare", at = @At("TAIL"))
    private void updateMobSize(PathNavigationRegion cachedWorld, Mob entity, CallbackInfo ci) {
        if (!ModConfig.get().general.pathPassengerAware || !entity.isVehicle()) return;

        Entity rider = entity;
        float height = (float) -entity.position().y;
        float maxWidth = entity.getBbWidth();
        while (rider.isVehicle()) {
            rider = rider.getFirstPassenger();
            maxWidth = Math.max(maxWidth, rider.getBbWidth());
        }
        height += (float) (rider.getBbHeight() + rider.position().y);

        this.entityWidth = Mth.ceil(maxWidth);
        this.entityHeight = Mth.ceil(height);
        this.entityDepth = this.entityWidth;
    }
}
