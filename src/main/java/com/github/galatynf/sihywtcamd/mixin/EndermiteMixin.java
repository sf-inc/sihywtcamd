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
import net.minecraft.util.math.MathHelper;
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
            int maxY = world.getBottomY() + world.getLogicalHeight() - 1;
            for (BlockPos pos : BlockPos.iterateRandomly(this.random, 16, oldPos, 8)) {
                if (target.hasVehicle()) {
                    target.stopRiding();
                }
                int y = MathHelper.clamp(pos.getY(), world.getBottomY(), maxY);
                if (!target.teleport(pos.getX() + 0.5, y, pos.getZ() + 0.5, true)) {
                    continue;
                }

                this.getEntityWorld().emitGameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Emitter.of(target));
                SoundEvent soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                this.getEntityWorld().playSound(null, oldPos.getX(), oldPos.getY(), oldPos.getZ(), soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
                target.playSound(soundEvent, 1.0f, 1.0f);
                target.onLanding();
                break;
            }
        }
    }
}
