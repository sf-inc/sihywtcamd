package com.github.galatynf.sihywtcamd.mixin.wither;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
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

    @ModifyReturnValue(method = "createWitherAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder increaseHealth(DefaultAttributeContainer.Builder original) {
        if (ModConfig.get().bosses.wither.increasedHealth) {
            original.add(EntityAttributes.MAX_HEALTH, 400.0);
        }
        return original;
    }

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void spawnWitherSkeletons(CallbackInfo ci) {
        if (this.getWorld().isClient()) {
            return;
        }
        if ((this.getWorld().getDifficulty().equals(Difficulty.NORMAL)
                    || this.getWorld().getDifficulty().equals(Difficulty.HARD))
                && this.getInvulnerableTimer() < 1
                && !MyComponents.WITHER_COMPONENT.get(this).wasHalfHealthReached()
                && this.getHealth() < this.getMaxHealth() / 2.0D) {
            if (ModConfig.get().bosses.wither.explosion) {
                this.getWorld().createExplosion(this, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, World.ExplosionSourceType.MOB);
            }
            if (ModConfig.get().bosses.wither.skeletonsSpawn) {
                int nbWitherSkeletons = 3 + Math.round(2 * this.getWorld().getLocalDifficulty(this.getBlockPos()).getClampedLocalDifficulty());
                float deltaAngle = (2 * MathHelper.PI) / nbWitherSkeletons;
                for (int i=0; i < nbWitherSkeletons; ++i) {
                    int x = MathHelper.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.cos(i * deltaAngle));
                    int z = MathHelper.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * MathHelper.sin(i * deltaAngle));
                    Optional<BlockPos> blockPos = BlockPos.findClosest(this.getBlockPos().add(x, 0, z), 3, 3,
                            pos -> this.getWorld().getBlockState(pos).isAir());
                    blockPos.ifPresent(pos -> EntityType.WITHER_SKELETON.spawn((ServerWorld) this.getWorld(), pos, SpawnReason.EVENT));
                }
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
