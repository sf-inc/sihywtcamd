package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.SkeletonSwimGoal;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonMobMixin extends MobEntityMixin {
    protected AbstractSkeletonMobMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void addGoals(CallbackInfo ci) {
        HostileEntity thisMob = (HostileEntity) (Object) this;
        if (ModConfig.get().skeletons.general.fleeGoal) {
            this.goalSelector.add(3, new FleeEntityGoal<>(thisMob, PlayerEntity.class, 4, 1.2, 1.5,
                    (livingEntity) -> (this.getMainHandStack().getItem().equals(Items.BOW))));
        }
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.add(3, new ActiveTargetGoal<>(thisMob, MerchantEntity.class, true));
        }
        if (ModConfig.get().skeletons.general.swimGoal) {
            this.goalSelector.add(2, new SkeletonSwimGoal(thisMob));
        }
    }

    @WrapOperation(method = "createArrowProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;createArrowProjectile(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;FLnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    protected PersistentProjectileEntity updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               ItemStack bow, Operation<PersistentProjectileEntity> original) {
        return original.call(entity, stack, damageModifier, bow);
    }
}
