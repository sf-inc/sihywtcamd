package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.BowQuickAttackGoal;
import com.github.galatynf.sihywtcamd.entity.SkeletonSwimGoal;
import com.github.galatynf.sihywtcamd.mixin.MobEntityMixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.SKELETON_BABY;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonMobMixin extends MobEntityMixin implements RangedAttackMob {
    @Shadow @Final private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;

    @Shadow public abstract void updateAttackType();

    @Unique
    private final Goal bowQuickAttackGoal = new BowQuickAttackGoal<>((HostileEntity & RangedAttackMob) (Object) this,
            1.0, 30, 3, 15f);

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

    @Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;updateAttackType()V"))
    private void canSpawnBaby(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                              NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        this.setBaby(ModConfig.get().skeletons.general.baby && this.random.nextFloat() < 0.1f);
    }

    @Inject(method = "updateAttackType", at = @At("TAIL"))
    private void replaceBowAttackGoal(CallbackInfo ci) {
        this.goalSelector.remove(this.bowQuickAttackGoal);
        if (!ModConfig.get().skeletons.general.babyQuickAttackGoal) return;

        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        if (this.isBaby() && itemStack.isOf(Items.BOW)) {
            this.goalSelector.remove(this.bowAttackGoal);
            this.goalSelector.add(4, this.bowQuickAttackGoal);
        }
    }

    @WrapOperation(method = "shootAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(DDDFF)V"))
    private void updateVelocityQuickAttackGoal(PersistentProjectileEntity instance, double x, double y, double z,
                                               float speed, float divergence, Operation<Void> original,
                                               LivingEntity target, float pullProgress) {
        if (ModConfig.get().skeletons.general.babyQuickAttackGoal && this.isBaby()) {
            original.call(instance, x, y, z, 2.2f * pullProgress, divergence + 2.f);
        } else {
            original.call(instance, x, y, z, speed, divergence);
        }
    }

    @Override
    protected void onSetBaby(boolean baby, CallbackInfo ci) {
        this.getDataTracker().set(SKELETON_BABY, baby);
        this.updateAttackType();
    }

    @Override
    protected void onInitDataTracker(CallbackInfo ci) {
        this.getDataTracker().startTracking(SKELETON_BABY, false);
    }

    @Override
    protected void readModDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.setBaby(nbt.getBoolean("IsBaby"));
    }

    @Override
    protected void writeModDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("IsBaby", this.isBaby());
    }

    @ModifyReturnValue(method = "getActiveEyeHeight", at = @At("RETURN"))
    private float updateSkeletonEyeHeight(float original) {
        if (ModConfig.get().skeletons.general.baby) {
            return original * this.getScaleFactor();
        } else {
            return original;
        }
    }



    @WrapOperation(method = "createArrowProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;createArrowProjectile(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/projectile/PersistentProjectileEntity;"))
    protected PersistentProjectileEntity updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               Operation<PersistentProjectileEntity> original) {
        return original.call(entity, stack, damageModifier);
    }
}
