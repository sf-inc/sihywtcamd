package com.github.galatynf.sihywtcamd.mixin.wither;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(WitherBoss.class)
public abstract class WitherMixin extends Monster {
    @Shadow public abstract int getInvulnerableTicks();

    @Unique
    static private final int sihywtcamd_SKELETONS_SPAWN_DISTANCE = 5;
    @Unique
    static private final float sihywtcamd_VANILLA_MAX_HEALTH = 300.0F;

    protected WitherMixin(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder increaseHealth(AttributeSupplier.Builder original) {
        if (ModConfig.get().bosses.wither.increasedHealth) {
            original.add(Attributes.MAX_HEALTH, 400.0);
        }
        return original;
    }

    @ModifyArg(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;heal(F)V", ordinal = 0))
    private float scaleSpawnHeal(float amount) {
        if (!ModConfig.get().bosses.wither.increasedHealth) {
            return amount;
        }
        return amount * this.getMaxHealth() / sihywtcamd_VANILLA_MAX_HEALTH;
    }

    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void spawnWitherSkeletons(CallbackInfo ci) {
        if (!(this.level() instanceof ServerLevel serverWorld)) {
            return;
        }
        if ((this.level().getDifficulty().equals(Difficulty.NORMAL)
                    || this.level().getDifficulty().equals(Difficulty.HARD))
                && this.getInvulnerableTicks() < 1
                && !MyComponents.WITHER_COMPONENT.get(this).wasHalfHealthReached()
                && this.getHealth() < this.getMaxHealth() / 2.0D) {
            if (ModConfig.get().bosses.wither.explosion) {
                this.level().explode(this, this.getX(), this.getEyeY(), this.getZ(), 7.0f, false, Level.ExplosionInteraction.MOB);
            }
            if (ModConfig.get().bosses.wither.skeletonsSpawn) {
                int nbWitherSkeletons = 3 + Math.round(2 * serverWorld.getCurrentDifficultyAt(this.blockPosition()).getSpecialMultiplier());
                float deltaAngle = (2 * Mth.PI) / nbWitherSkeletons;
                for (int i=0; i < nbWitherSkeletons; ++i) {
                    int x = Mth.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * Mth.cos(i * deltaAngle));
                    int z = Mth.floor(sihywtcamd_SKELETONS_SPAWN_DISTANCE * Mth.sin(i * deltaAngle));
                    BlockPos center = this.blockPosition().offset(x, 0, z);
                    Optional<BlockPos> blockPos = BlockPos.findClosestMatch(center, 3, 6,
                                    pos -> serverWorld.getBlockState(pos.below()).isFaceSturdy(serverWorld, pos.below(), Direction.UP)
                                            && this.hasRoomForSkeleton(serverWorld, pos))
                            .or(() -> BlockPos.findClosestMatch(center, 3, 3, pos -> this.hasRoomForSkeleton(serverWorld, pos)));
                    blockPos.ifPresent(pos -> EntityType.WITHER_SKELETON.spawn(serverWorld, pos, EntitySpawnReason.EVENT));
                }
            }

            MyComponents.WITHER_COMPONENT.get(this).setHalfHealthReached();
            if (ModConfig.get().general.enableDebugInfo) {
                this.setCustomName(Component.nullToEmpty("Half Health Reached"));
                this.setCustomNameVisible(true);
            }
        }
        if (ModConfig.get().bosses.wither.stormyWeather
                && serverWorld.getGameRules().get(GameRules.ADVANCE_WEATHER)) {
            serverWorld.getServer().setWeatherParameters(0, 50, true, true);
        }
    }

    @Unique
    private boolean hasRoomForSkeleton(ServerLevel world, BlockPos pos) {
        return world.noCollision(null, EntityType.WITHER_SKELETON.getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), true);
    }
}
