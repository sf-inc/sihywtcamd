package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonMixin extends HostileEntity {
    protected AbstractSkeletonMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void fleePlayerS(CallbackInfo ci) {
        if (ModConfig.get().skeleton.general.fleeGoal) {
            this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 4, 1.2, 1.5,
                    (livingEntity) -> (this.getMainHandStack().getItem().equals(Items.BOW))));
        }
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void targetMerchantS(CallbackInfo ci) {
        if (ModConfig.get().overworld.general.merchantHostility) {
            this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        }
    }
}
