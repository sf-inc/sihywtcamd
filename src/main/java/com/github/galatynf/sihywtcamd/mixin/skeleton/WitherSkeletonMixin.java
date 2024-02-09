package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
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

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return (ModConfig.get().skeletons.witherSkeleton.fireResistant && damageSource.isIn(DamageTypeTags.IS_FIRE))
                || super.isInvulnerableTo(damageSource);
    }

    @Inject(method = "initialize", at = @At("TAIL"))
    private void canSpawnBaby(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                              NbtCompound entityTag, CallbackInfoReturnable<EntityData> cir) {
        this.setBaby(ModConfig.get().skeletons.witherSkeleton.baby && this.random.nextFloat() < 0.2F);
    }

    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    private void useBetterEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        if (ModConfig.get().skeletons.witherSkeleton.bow && !this.isBaby()
                && random.nextFloat() < 0.25F) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
            ci.cancel();
        } else if (ModConfig.get().skeletons.witherSkeleton.netheriteSword) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
            this.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.025f);
            ci.cancel();
        }
    }

    @Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
    private void makeEyeHeightDependsOnSize(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * this.getScaleFactor());
    }
}
