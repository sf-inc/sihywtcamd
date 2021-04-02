package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public abstract class SlimeMixin extends MobEntity {
    @Shadow public abstract int getSize();

    @Shadow protected abstract void setSize(int size, boolean heal);

    protected SlimeMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        int i = this.random.nextInt(3);
        if ((ModConfig.get().slimeBiggerSize || i < 2)
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
                && ModConfig.get().slimeCanMerge
                && this.isAlive()
                && this.getSize() < 4
                && (this.world.getTime() % 5) == 0) {
            SlimeEntity slimeEntity = this.world.getClosestEntity(SlimeEntity.class, TargetPredicate.DEFAULT, this,
                    this.getX(), this.getY(), this.getZ(), this.getBoundingBox());
            if (slimeEntity != null
                    && this.getSize() == slimeEntity.getSize()) {
                slimeEntity.remove();
                this.setSize(this.getSize() * 2, true);
            }
        }
    }
}
