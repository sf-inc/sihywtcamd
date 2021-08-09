package com.github.galatynf.sihywtcamd.mixin.spider;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SpiderEntity.class)
public class SpiderMixin extends HostileEntity {
    private static final TrackedData<Boolean> BABY = DataTracker.registerData(SpiderEntity .class, TrackedDataHandlerRegistry.BOOLEAN);

    protected SpiderMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    private void targetMerchantSp(CallbackInfo ci) {
        if (ModConfig.get().overworld.merchantHostility) {
            this.targetSelector.add(3, new SpiderEntity.FollowTargetGoal<>((SpiderEntity) (Object) this, MerchantEntity.class));
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void addBabyData(CallbackInfo ci) {
        this.getDataTracker().startTracking(BABY, false);
    }

    @Override
    public boolean isBaby() {
        return this.getDataTracker().get(BABY);
    }

    @Override
    public void setBaby(boolean baby) {
        this.getDataTracker().set(BABY, baby);
        if (baby) {
            EntityAttributeInstance attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            EntityAttributeInstance maxHealth = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

            Objects.requireNonNull(attackDamage).addPersistentModifier(new EntityAttributeModifier(
                    "Baby spawn malus", -0.5F * attackDamage.getValue(), EntityAttributeModifier.Operation.ADDITION));
            Objects.requireNonNull(maxHealth).addPersistentModifier(new EntityAttributeModifier(
                    "Baby spawn malus", -0.5F * maxHealth.getValue(), EntityAttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (BABY.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        this.setBaby(tag.getBoolean("IsBaby"));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("IsBaby", this.isBaby());
    }

    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.33F : 1.0F;
    }

    @Override
    public float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.65F * this.getScaleFactor();
    }
}
