package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(Endermite.class)
public abstract class EndermiteMixin extends MobEntityMixin {
    @Unique
    private static final Identifier BONUS_ID = id("random_endermite_bonus");

    protected EndermiteMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void onTryAttackSuccess(ServerLevel world, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().end.endermite.teleportAttack
                && entity instanceof LivingEntity target) {
            BlockPos oldPos = target.blockPosition();
            Entity vehicle = target.getVehicle();
            boolean teleported = false;
            if (vehicle != null) {
                target.stopRiding();
            }
            int maxY = world.getMinY() + world.getLogicalHeight() - 1;
            for (BlockPos pos : BlockPos.randomInCube(this.random, 16, oldPos, 8)) {
                int y = Mth.clamp(pos.getY(), world.getMinY(), maxY);
                if (!target.randomTeleport(pos.getX() + 0.5, y, pos.getZ() + 0.5, true)) {
                    continue;
                }

                this.level().gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(target));
                SoundEvent soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                this.level().playSound(null, oldPos.getX(), oldPos.getY(), oldPos.getZ(), soundEvent, SoundSource.PLAYERS, 1.0f, 1.0f);
                target.playSound(soundEvent, 1.0f, 1.0f);
                target.resetFallDistance();
                teleported = true;
                break;
            }
            if (!teleported && vehicle != null && vehicle.isAlive()) {
                target.startRiding(vehicle, true, false);
            }
        }
    }
}
