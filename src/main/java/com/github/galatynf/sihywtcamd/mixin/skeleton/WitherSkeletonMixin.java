package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonMixin extends AbstractSkeletonEntity {
    protected WitherSkeletonMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WitherSkeletonEntity;updateAttackType()V"))
    private void canSpawnBaby(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                              EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        this.setBaby(ModConfig.get().skeletons.witherSkeleton.baby && this.random.nextFloat() < 0.2F);
    }

    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    private void useBetterEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (ModConfig.get().skeletons.witherSkeleton.bow && !this.isBaby()
                && random.nextFloat() < 0.25F) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            ci.cancel();
        } else if (ModConfig.get().skeletons.witherSkeleton.ironSword) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.05f);
            ci.cancel();
        }
    }
}
