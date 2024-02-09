package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.SkeletonSwimGoal;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonMixin extends HostileEntity {
    @Unique
    private static final TrackedData<Boolean> BABY = DataTracker.registerData(AbstractSkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected AbstractSkeletonMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void fleePlayerS(CallbackInfo ci) {
        if (ModConfig.get().skeletons.general.fleeGoal) {
            this.goalSelector.add(3, new FleeEntityGoal<>(this, PlayerEntity.class, 4, 1.2, 1.5,
                    (livingEntity) -> (this.getMainHandStack().getItem().equals(Items.BOW))));
        }
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void targetMerchantS(CallbackInfo ci) {
        if (ModConfig.get().overworld.general.merchantHostility) {
            this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        }
        if (ModConfig.get().skeletons.general.swimGoal) {
            this.goalSelector.add(2, new SkeletonSwimGoal(this));
        }
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
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (BABY.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
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
