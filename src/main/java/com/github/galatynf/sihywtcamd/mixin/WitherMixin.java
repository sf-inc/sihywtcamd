package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WitherEntity.class)
public abstract class WitherMixin extends HostileEntity {
    @Shadow public abstract int getInvulnerableTimer();
    static private final int sihywtcamd_SKELETONS_SPAWN_DISTANCE = 5;
    @Unique
    private boolean sihywtcamd_hasSpawned = false;

    protected WitherMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        EntityAttributeInstance instance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (instance != null && ModConfig.get().boss.wither.increasedHealth) {
            instance.setBaseValue(400.0D);
            this.setHealth(this.getMaxHealth());
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void spawnWitherSkeletons(CallbackInfo ci) {
        if (this.world.isClient()) {
            return;
        }
        if (ModConfig.get().boss.wither.skeletonsSpawn
                && (this.world.getDifficulty().equals(Difficulty.NORMAL)
                    || this.world.getDifficulty().equals(Difficulty.HARD))
                && this.getInvulnerableTimer() < 1
                && !sihywtcamd_hasSpawned
                && this.getHealth() < this.getMaxHealth() / 2.0D) {
            if (ModConfig.get().boss.wither.explosion) {
                this.world.createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, World.ExplosionSourceType.MOB);
            }
            int nbWitherSkeletons = 3 + Math.round(2 * this.world.getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            float deltaAngle = (2 * MathHelper.PI) / nbWitherSkeletons;
            for (int i=0; i < nbWitherSkeletons; ++i) {
                float x = sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.cos(i * deltaAngle);
                float z = sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.sin(i * deltaAngle);
                Optional<BlockPos> blockPos = BlockPos.findClosest(this.getBlockPos().add(x, 0, z), 3, 3, pos -> this.world.getBlockState(pos).isAir());
                blockPos.ifPresent(pos -> EntityType.WITHER_SKELETON.spawn((ServerWorld) this.world, pos, SpawnReason.EVENT));
            }

            sihywtcamd_hasSpawned = true;
        }
        if (ModConfig.get().boss.wither.stormyWeather) {
            ((ServerWorld) this.world).setWeather(0, 50, true, true);
        }
    }
}
