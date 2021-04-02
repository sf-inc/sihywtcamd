package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherEntity.class)
public abstract class WitherMixin extends HostileEntity {
    @Shadow public abstract int getInvulnerableTimer();

    @Unique
    private boolean sihywtcamd_hasSpawned = false;

    protected WitherMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createWitherAttributes", at = @At("HEAD"), cancellable = true)
    private static void changeHealth(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        if (ModConfig.get().witherIncreasedHealth) {
            cir.setReturnValue(HostileEntity.createHostileAttributes()
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 400.0D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6000000238418579D)
                    .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
                    .add(EntityAttributes.GENERIC_ARMOR, 4.0D));
        }
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void spawnWitherSkeletons(CallbackInfo ci) {
        if (!this.world.isClient
                && ModConfig.get().witherSpawnSkeletons
                && (this.world.getDifficulty().equals(Difficulty.NORMAL) || this.world.getDifficulty().equals(Difficulty.HARD))
                && this.getInvulnerableTimer() < 1
                && !sihywtcamd_hasSpawned
                && this.getHealth() < this.getMaxHealth() / 2.0D) {
            int witherSummoned = 3;
            if (this.random.nextFloat() < this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty()) {
                ++witherSummoned;
            }
            for (int i=0; i < witherSummoned; i++) {
                EntityType.WITHER_SKELETON.spawn((ServerWorld) this.world, null, null, null, this.getBlockPos(),
                        SpawnReason.EVENT, false, false);
            }

            sihywtcamd_hasSpawned = true;
        }
    }
}
