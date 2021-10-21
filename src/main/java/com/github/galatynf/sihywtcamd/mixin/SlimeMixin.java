package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public abstract class SlimeMixin extends MobEntity {
    private static final TrackedData<Boolean> MERGED = DataTracker.registerData(SlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Shadow public abstract int getSize();

    @Shadow protected abstract void setSize(int size, boolean heal);

    @Shadow public abstract EntityType<? extends SlimeEntity> getType();

    protected SlimeMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void addMergedData(CallbackInfo ci) {
        this.getDataTracker().startTracking(MERGED, false);
    }

    public boolean hasMerged() {
        return this.getDataTracker().get(MERGED);
    }

    public void setHasMerged(boolean merged) {
        this.getDataTracker().set(MERGED, merged);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readMergedData(NbtCompound nbt, CallbackInfo ci) {
        this.setHasMerged(nbt.getBoolean("hasMerged"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeMergedData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("hasMerged", this.hasMerged());
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        int i = this.random.nextInt(3);
        if (((ModConfig.get().overworld.slimes.slimeBiggerSize && this.getType().equals(EntityType.SLIME)) || i < 2)
                && this.random.nextFloat() < 0.5F * difficulty.getClampedLocalDifficulty()) {
            ++i;
        }

        int j = 1 << i;
        this.setSize(j, true);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tryToMerge(CallbackInfo ci) {
        if (!this.world.isClient
                && this.getType().equals(EntityType.SLIME)
                && ModConfig.get().overworld.slimes.slimeCanMerge
                && !this.hasMerged()
                && this.isAlive()
                && this.getSize() < 4
                && (this.world.getTime() % 5) == 0) {
            SlimeEntity slimeEntity = this.world.getClosestEntity(SlimeEntity.class, TargetPredicate.DEFAULT, this,
                    this.getX(), this.getY(), this.getZ(), this.getBoundingBox());
            if (slimeEntity != null
                    && this.getSize() == slimeEntity.getSize()) {
                this.setHasMerged(true);
                slimeEntity.remove(RemovalReason.DISCARDED);
                this.setSize(this.getSize() * 2, true);
            }
        }
    }

    @ModifyVariable(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;setInvulnerable(Z)V"))
    private SlimeEntity transferMergedGene(SlimeEntity slime) {
        slime.getDataTracker().set(MERGED, this.getDataTracker().get(MERGED));
        return slime;
    }
}
