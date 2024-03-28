package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.BowQuickAttackGoal;
import com.github.galatynf.sihywtcamd.entity.SkeletonSwimGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonMixin extends HostileEntity implements RangedAttackMob {
    @Shadow @Final private BowAttackGoal<AbstractSkeletonEntity> bowAttackGoal;

    @Shadow public abstract void updateAttackType();

    @Unique
    private final Goal bowQuickAttackGoal = new BowQuickAttackGoal<>(this, 1.0, 30, 3, 15f);
    @Unique
    private static final TrackedData<Boolean> BABY = DataTracker.registerData(AbstractSkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected AbstractSkeletonMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void addGoals(CallbackInfo ci) {
        if (ModConfig.get().skeletons.general.fleeGoal) {
            this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 4, 1.2, 1.5,
                    (livingEntity) -> (this.getMainHandStack().getItem().equals(Items.BOW))));
        }
        if (ModConfig.get().general.merchantHostility) {
            this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        }
        if (ModConfig.get().skeletons.general.swimGoal) {
            this.goalSelector.add(2, new SkeletonSwimGoal(this));
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

    @ModifyVariable(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private PersistentProjectileEntity updateVelocityQuickAttackGoal(PersistentProjectileEntity persistentProjectileEntity,
                                                                     LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        if (ModConfig.get().skeletons.general.babyQuickAttackGoal
                && this.isBaby() && itemStack.isOf(Items.BOW)) {
            double d = target.getX() - this.getX();
            double e = target.getBodyY(0.3333333333333333) - persistentProjectileEntity.getY();
            double f = target.getZ() - this.getZ();
            double g = Math.sqrt(d * d + f * f);
            persistentProjectileEntity.setVelocity(d, e + g * 0.2, f, 2.2f * pullProgress, 16 - this.getWorld().getDifficulty().getId() * 4);
        }
        return persistentProjectileEntity;
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();
        this.getDataTracker().startTracking(BABY, false);
    }

    @Override
    public boolean isBaby() {
        return this.getDataTracker().get(BABY);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getDataTracker().set(BABY, baby);
        this.updateAttackType();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (BABY.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;updateAttackType()V"))
    private void readBabyData(NbtCompound nbt, CallbackInfo ci) {
        this.setBaby(nbt.getBoolean("IsBaby"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsBaby", this.isBaby());
    }

    @Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
    private void makeEyeHeightDependsOnSize(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() * this.getScaleFactor());
    }
}
