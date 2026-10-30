package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Slimes, magma cubes and sulfur cubes share AbstractCubeMob since 26.2: slime only behaviours check for Slime
@Mixin(AbstractCubeMob.class)
public abstract class SlimeMixin extends Mob {
    @Shadow public abstract int getSize();
    @Shadow public abstract EntityType<? extends AbstractCubeMob> getType();
    @Shadow protected abstract ParticleOptions getParticleType();

    @Shadow public abstract void setSize(int size, boolean heal);

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;setSpawnSize(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;)V", shift = At.Shift.AFTER))
    private void spawnBigger(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                             SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().overworld.slime.biggerSize
                && (Object) this instanceof Slime
                && this.random.nextFloat() < 0.05f + 0.05f * difficulty.getSpecialMultiplier()) {
            this.setSize(8, true);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    protected void onTick(CallbackInfo ci) {
        if (!((Object) this instanceof Slime)) return;

        MyComponents.SLIME_COMPONENT.get(this).updateMerged();

        if (!this.level().isClientSide()
                && this.isAlive()
                && ModConfig.get().overworld.slime.magmaConversion
                && this.level().dimension().equals(Level.NETHER)
                && this.isInLava()) {
            int size = this.getSize();
            MagmaCube magmaCube = this.convertTo(
                    EntityTypes.MAGMA_CUBE,
                    ConversionParams.single(this, false, true),
                    slime -> {});
            if (magmaCube != null) {
                magmaCube.setSize(size, true);
                this.playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.level().getEntitiesOfClass(ServerPlayer.class, magmaCube.getBoundingBox().inflate(16.0),
                                player -> !player.isSpectator() && player.hasLineOfSight(magmaCube))
                        .forEach(AdvancementRegistry.SLIME_TO_MAGMA_CONVERSION::trigger);
            }
        }
    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
    private void tryToMerge(Entity entity, CallbackInfo ci) {
        if (!((Object) this instanceof Slime)
                || !(entity instanceof Slime otherSlime)
                || !this.getType().equals(entity.getType())) return;

        if (!this.level().isClientSide()
                && ModConfig.get().overworld.slime.canMerge
                && MyComponents.SLIME_COMPONENT.get(this).canMerge()
                && MyComponents.SLIME_COMPONENT.get(otherSlime).canMerge()
                && this.isAlive()
                && otherSlime.isAlive()
                && !otherSlime.hasCustomName()
                && !otherSlime.isPersistenceRequired()
                && this.getSize() < 4
                && this.getSize() == otherSlime.getSize()) {
            MyComponents.SLIME_COMPONENT.get(this).setMerged();
            otherSlime.discard();
            this.setSize(this.getSize() * 2, true);
            ((ServerLevel) this.level()).sendParticles(this.getParticleType(), this.getX(), this.getY(), this.getZ(),
                    this.getSize() * 8, this.getBbWidth() * 0.25, 0.0, this.getBbWidth() * 0.25, 0.0);
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0f,
                    (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);

            if (ModConfig.get().general.enableDebugInfo) {
                this.setCustomName(Component.nullToEmpty("Merged"));
                this.setCustomNameVisible(true);
            }
        }
    }

    @WrapOperation(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;convertTo(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/ConversionParams;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/world/entity/ConversionParams$AfterConversion;)Lnet/minecraft/world/entity/Mob;"))
    private Mob transferMergedGene(AbstractCubeMob instance, EntityType<?> entityType,
                                   ConversionParams entityConversionContext, EntitySpawnReason spawnReason,
                                   ConversionParams.AfterConversion<AbstractCubeMob> finalizer, Operation<Mob> original) {
        if (!(instance instanceof Slime)) {
            return original.call(instance, entityType, entityConversionContext, spawnReason, finalizer);
        }

        return original.call(instance, entityType, entityConversionContext, spawnReason,
                (ConversionParams.AfterConversion<AbstractCubeMob>) slime -> {
            finalizer.finalizeConversion(slime);
            boolean hasMerged = MyComponents.SLIME_COMPONENT.get(instance).hasMerged();
            if (hasMerged) {
                MyComponents.SLIME_COMPONENT.get(slime).setMerged();
            }

            if (ModConfig.get().general.enableDebugInfo && hasMerged) {
                slime.setCustomName(Component.nullToEmpty("Merged Child"));
                slime.setCustomNameVisible(true);
            }
        });
    }

    @Inject(method = "dealDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/cubemob/AbstractCubeMob;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    protected void onDamage(LivingEntity target, CallbackInfo ci) {

    }
}
