package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
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
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SlimeEntity.class)
public abstract class SlimeMixin extends MobEntity {
    @Unique
    private static final TrackedData<Boolean> MERGED = DataTracker.registerData(SlimeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Shadow public abstract int getSize();

    @Shadow public abstract void setSize(int size, boolean heal);

    @Shadow public abstract EntityType<? extends SlimeEntity> getType();

    @Shadow protected abstract ParticleEffect getParticles();

    protected SlimeMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void addMergedData(CallbackInfo ci) {
        this.getDataTracker().startTracking(MERGED, false);
    }

    @Unique
    public boolean hasMerged() {
        return this.getDataTracker().get(MERGED);
    }

    @Unique
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

    @Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/entity/EntityData;"))
    private void spawnBigger(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData,
                             NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (ModConfig.get().overworld.slime.biggerSize
                && this.random.nextFloat() < 0.05f + 0.05f * difficulty.getClampedLocalDifficulty()) {
            this.setSize(8, true);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tryToMerge(CallbackInfo ci) {
        if (!this.getWorld().isClient()
                && this.getType().equals(EntityType.SLIME)
                && ModConfig.get().overworld.slime.canMerge
                && !this.hasMerged()
                && this.isAlive()
                && this.getSize() < 4
                && (this.getWorld().getTime() % 5) == 0) {
            SlimeEntity slimeEntity = this.getWorld().getClosestEntity(SlimeEntity.class, TargetPredicate.DEFAULT, this,
                    this.getX(), this.getY(), this.getZ(), this.getBoundingBox());
            if (slimeEntity != null
                    && this.getSize() == slimeEntity.getSize()) {
                this.setCustomName(Text.of("Merged"));
                this.setCustomNameVisible(Sihywtcamd.DEBUG);
                this.setHasMerged(true);
                slimeEntity.remove(RemovalReason.DISCARDED);
                this.setSize(this.getSize() * 2, true);
                this.getWorld().addParticle(this.getParticles(), this.getX(), this.getY(), this.getZ(),
                        0.0, 0.0, 0.0);
                this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0f,
                        (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
        }
    }

    @ModifyVariable(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SlimeEntity;setInvulnerable(Z)V"))
    private SlimeEntity transferMergedGene(SlimeEntity slime, RemovalReason value) {
        this.setCustomName(Text.of("Merged Child"));
        this.setCustomNameVisible(Sihywtcamd.DEBUG);
        slime.getDataTracker().set(MERGED, this.getDataTracker().get(MERGED));
        return slime;
    }
}
