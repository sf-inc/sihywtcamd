package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.DisableableFollowTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchEntity.class)
public abstract class WitchMixin extends RaiderEntity {
    protected WitchMixin(EntityType<? extends RaiderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void fleePlayerW(CallbackInfo ci) {
        if (ModConfig.get().overworld.illagers.witchFleeGoal) {
            this.goalSelector.add(1, new FleeEntityGoal<>(this, PlayerEntity.class, 3, 1.2, 1.5,
                    (livingEntity) -> true));
        }
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void targetMerchantW(CallbackInfo ci) {
        if (ModConfig.get().overworld.mobs.merchantHostility) {
            this.targetSelector.add(3, new DisableableFollowTargetGoal<>(this, MerchantEntity.class, 10,
                    true, false, null));
        }
    }
}
