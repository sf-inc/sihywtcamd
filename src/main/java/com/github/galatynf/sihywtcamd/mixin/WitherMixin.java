package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
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
import net.minecraft.text.Text;
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
    @Unique
    static private final int sihywtcamd_SKELETONS_SPAWN_DISTANCE = 5;

    protected WitherMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        EntityAttributeInstance instance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (instance != null && ModConfig.get().bosses.wither.increasedHealth) {
            instance.setBaseValue(400.0D);
            this.setHealth(this.getMaxHealth());
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void spawnWitherSkeletons(CallbackInfo ci) {
        if (this.getWorld().isClient()) {
            return;
        }
        if (ModConfig.get().bosses.wither.skeletonsSpawn
                && (this.getWorld().getDifficulty().equals(Difficulty.NORMAL)
                    || this.getWorld().getDifficulty().equals(Difficulty.HARD))
                && this.getInvulnerableTimer() < 1
                && !MyComponents.WITHER_COMPONENT.get(this).wasHalfHealthReached()
                && this.getHealth() < this.getMaxHealth() / 2.0D) {
            if (ModConfig.get().bosses.wither.explosion) {
                this.getWorld().createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, World.ExplosionSourceType.MOB);
            }
            int nbWitherSkeletons = 3 + Math.round(2 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
            float deltaAngle = (2 * MathHelper.PI) / nbWitherSkeletons;
            for (int i=0; i < nbWitherSkeletons; ++i) {
                int x = MathHelper.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.cos(i * deltaAngle));
                int z = MathHelper.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.sin(i * deltaAngle));
                Optional<BlockPos> blockPos = BlockPos.findClosest(this.getBlockPos().add(x, 0, z), 3, 3,
                        pos -> this.getWorld().getBlockState(pos).isAir());
                blockPos.ifPresent(pos -> EntityType.WITHER_SKELETON.spawn((ServerWorld) this.getWorld(), pos, SpawnReason.EVENT));
            }

            MyComponents.WITHER_COMPONENT.get(this).setHalfHealthReached();
            if (Sihywtcamd.DEBUG) {
                this.setCustomName(Text.of("Half Health Reached"));
                this.setCustomNameVisible(true);
            }
        }
        if (ModConfig.get().bosses.wither.stormyWeather) {
            ((ServerWorld) this.getWorld()).setWeather(0, 50, true, true);
        }
    }
}
