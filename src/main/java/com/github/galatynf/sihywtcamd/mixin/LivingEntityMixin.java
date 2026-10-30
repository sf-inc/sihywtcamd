package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Unique
    private static final Identifier RIDDEN_HORSE_SPEED_ID = id("ridden_horse_speed");

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow public float yBodyRot;

    @Shadow public abstract boolean isBaby();
    @Shadow @Nullable public abstract LivingEntity getLastHurtByMob();
    @Shadow @Nullable public abstract AttributeInstance getAttribute(Holder<Attribute> attribute);
    @Shadow public abstract float getMaxHealth();
    @Shadow public abstract void setHealth(float health);

    @Inject(method = "calculateFallDamage", at = @At("HEAD"), cancellable = true)
    private void cancelArthropodFallDamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cir) {
        if (ModConfig.get().arthropods.general.noFallDamage && this.getType().is(EntityTypeTags.ARTHROPOD)) {
            cir.setReturnValue(0);
        }
    }

    @WrapOperation(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    private void healUndeadAttacker(LivingEntity instance, ServerLevel world, DamageSource source, float amount,
                                    Operation<Void> original) {
        float healthBefore = instance.getHealth() + instance.getAbsorptionAmount();
        original.call(instance, world, source, amount);

        if (ModConfig.get().undead.general.attackHeal
                && source.getEntity() instanceof LivingEntity attacker
                && attacker.getType().is(EntityTypeTags.UNDEAD)
                && !(attacker instanceof WitherBoss)) {
            float healthLost = healthBefore - instance.getHealth() - instance.getAbsorptionAmount();
            if (healthLost > 0) {
                attacker.heal(healthLost);
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void updateRiddenHorseSpeed(CallbackInfo ci) {
        if (this.level().isClientSide()
                || !(this.getType().equals(EntityType.SKELETON_HORSE) || this.getType().equals(EntityType.ZOMBIE_HORSE))) {
            return;
        }
        AttributeInstance movementSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeed == null) return;

        double multiplier = this.getRiddenHorseSpeedMultiplier();
        if (multiplier > 1.0) {
            if (!movementSpeed.hasModifier(RIDDEN_HORSE_SPEED_ID)) {
                // Mob ground velocity scales with the square of the movement speed
                movementSpeed.addTransientModifier(new AttributeModifier(RIDDEN_HORSE_SPEED_ID,
                        Math.sqrt(multiplier) - 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        } else {
            movementSpeed.removeModifier(RIDDEN_HORSE_SPEED_ID);
        }
    }

    @Unique
    private double getRiddenHorseSpeedMultiplier() {
        if (this.hasControllingPassenger() && this.getFirstPassenger() instanceof Mob) {
            if (ModConfig.get().skeletons.skeletonHorse.increasedSpeed
                    && this.getType().equals(EntityType.SKELETON_HORSE)) {
                return 2.0;
            } else if (ModConfig.get().zombies.zombieHorse.increasedSpeed
                    && this.getType().equals(EntityType.ZOMBIE_HORSE)
                    && ((ZombieHorseIMixin) this).sihywtcamd$isTrapHorse()) {
                return 2.5;
            }
        }
        return 1.0;
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    protected void onTickRiding(CallbackInfo ci) {

    }

    @Inject(method = "push(Lnet/minecraft/world/entity/Entity;)V", at = @At("TAIL"))
    protected void onPushAwayFrom(Entity entity, CallbackInfo ci) {

    }
}
