package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(EndermiteEntity.class)
public abstract class EndermiteMixin extends MobEntityMixin {
    @Unique
    private static final Identifier BONUS_ID = id("random_endermite_bonus");

    protected EndermiteMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onTryAttackSuccess(ServerWorld world, Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().end.endermite.teleportAttack
                && entity instanceof LivingEntity target) {
            BlockPos oldPos = target.getBlockPos();
            for (BlockPos pos : BlockPos.iterateRandomly(this.random, 8, oldPos, 8)) {
                if (target.hasVehicle()) {
                    target.stopRiding();
                }
                if (!target.teleport(pos.getX(), pos.getY(), pos.getZ(), true)) {
                    continue;
                }

                this.getWorld().emitGameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Emitter.of(target));
                SoundEvent soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                this.getWorld().playSound(null, oldPos.getX(), oldPos.getY(), oldPos.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
                target.playSound(soundEvent, 1.0f, 1.0f);
                break;
            }
        }
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (!ModConfig.get().arthropods.general.larvaeSpeedBonus) return;

        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if (speed != null) {
            double random = 0.25 * world.getRandom().nextDouble();
            double localDifficulty = 0.25 * difficulty.getClampedLocalDifficulty();
            speed.addPersistentModifier(new EntityAttributeModifier(
                    BONUS_ID,  random + localDifficulty, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }
}
