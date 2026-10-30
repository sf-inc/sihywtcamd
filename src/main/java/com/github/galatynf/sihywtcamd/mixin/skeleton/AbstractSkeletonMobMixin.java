package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.SkeletonSwimGoal;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMobMixin extends MobEntityMixin {
    protected AbstractSkeletonMobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("HEAD"))
    private void addGoals(CallbackInfo ci) {
        Monster thisMob = (Monster) (Object) this;
        if (ModConfig.get().skeletons.general.fleeGoal) {
            this.goalSelector.addGoal(3, new AvoidEntityGoal<>(thisMob, Player.class, 4, 1.2, 1.5,
                    (livingEntity) -> (this.getMainHandItem().getItem().equals(Items.BOW))));
        }
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(thisMob, AbstractVillager.class, true));
        }
        if (ModConfig.get().skeletons.general.swimGoal) {
            this.goalSelector.addGoal(2, new SkeletonSwimGoal(thisMob));
        }
    }

    @WrapOperation(method = "getArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getMobArrow(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;FLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;"))
    protected AbstractArrow updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               ItemStack bow, Operation<AbstractArrow> original) {
        return original.call(entity, stack, damageModifier, bow);
    }
}
