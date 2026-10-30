package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Skeleton.class)
public abstract class SkeletonMixin extends AbstractSkeletonMobMixin {
    protected SkeletonMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected AbstractArrow updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               ItemStack bow, Operation<AbstractArrow> original) {
        return MyComponents.SKELETON_COMPONENT.get(this).isSpectral()
                ? original.call(entity, new ItemStack(Items.SPECTRAL_ARROW), damageModifier, bow)
                : original.call(entity, stack, damageModifier, bow);
    }

    @Override
    protected void onInitialize(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().skeletons.skeleton.spectralArrow
                && world.getRandom().nextFloat() < 0.05f) {
            MyComponents.SKELETON_COMPONENT.get(this).setSpectral();
        }
    }
}
