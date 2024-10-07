package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonMixin extends AbstractSkeletonMobMixin {
    protected SkeletonMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected PersistentProjectileEntity updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               ItemStack bow, Operation<PersistentProjectileEntity> original) {
        return MyComponents.SKELETON_COMPONENT.get(this).isSpectral()
                ? original.call(entity, new ItemStack(Items.SPECTRAL_ARROW), damageModifier, bow)
                : original.call(entity, stack, damageModifier, bow);
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().skeletons.skeleton.spectralArrow
                && world.getRandom().nextFloat() < 0.05f) {
            MyComponents.SKELETON_COMPONENT.get(this).setSpectral();
        }
    }
}
