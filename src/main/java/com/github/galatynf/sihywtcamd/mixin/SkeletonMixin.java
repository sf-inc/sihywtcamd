package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class SkeletonMixin extends HostileEntity {
    protected SkeletonMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at=@At("HEAD"), method = "initGoals")
    private void fleePlayer(CallbackInfo ci) {
        this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 2, 1.5, 1.7, (livingEntity) -> (this.getMainHandStack().getItem() == Items.BOW)));
    }
}
