package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.SpectralSkeletonIMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonMixin extends AbstractSkeletonMobMixin implements SpectralSkeletonIMixin {
    @Unique
    private static final TrackedData<Boolean> SPECTRAL = DataTracker.registerData(SkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected SkeletonMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void addSpectralDataRead(NbtCompound nbt, CallbackInfo ci) {
        this.getDataTracker().set(SPECTRAL, nbt.getBoolean("HasSpectralArrows"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void addSpectralDataWrite(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("HasSpectralArrows", this.getDataTracker().get(SPECTRAL));
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initSpectralData(CallbackInfo ci) {
        this.getDataTracker().startTracking(SPECTRAL, false);
    }

    @Override
    protected PersistentProjectileEntity updateArrowProjectile(LivingEntity entity, ItemStack stack, float damageModifier,
                                                               Operation<PersistentProjectileEntity> original) {
        return this.getDataTracker().get(SPECTRAL)
                ? original.call(entity, new ItemStack(Items.SPECTRAL_ARROW), damageModifier)
                : original.call(entity, stack, damageModifier);
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().skeletons.skeleton.spectralArrow
                && world.getRandom().nextFloat() < 0.05f) {
            this.sihywtcamd$setSpectral();
        }
    }

    @Override
    public void sihywtcamd$setSpectral() {
        this.getDataTracker().set(SPECTRAL, true);

        if (Sihywtcamd.DEBUG) {
            this.setCustomName(Text.of("Spectral"));
            this.setCustomNameVisible(true);
        }
    }
}
