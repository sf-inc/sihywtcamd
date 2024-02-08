package com.github.galatynf.sihywtcamd.mixin.skeleton;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.SpectralSkeletonIMixin;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonMixin extends AbstractSkeletonEntity implements SpectralSkeletonIMixin {
    @Unique
    private static final TrackedData<Boolean> SPECTRAL = DataTracker.registerData(SkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected SkeletonMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
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
    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        if (this.getDataTracker().get(SPECTRAL)) {
            arrow = new ItemStack(Items.SPECTRAL_ARROW);
        }
        return super.createArrowProjectile(arrow, damageModifier);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (ModConfig.get().skeletons.skeleton.spectralArrow
                && world.getRandom().nextFloat() < 0.05f) {
            this.sihywtcamd$setSpectral();
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
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
