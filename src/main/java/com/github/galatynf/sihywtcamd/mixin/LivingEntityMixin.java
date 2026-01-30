package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public float bodyYaw;

    @Shadow public abstract boolean isBaby();
    @Shadow public abstract boolean isInvulnerableTo(ServerWorld world, DamageSource source);
    @Shadow @Nullable public abstract LivingEntity getAttacker();
    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(RegistryEntry<EntityAttribute> attribute);

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    private void cancelArthropodFallDamage(double fallDistance, float damagePerDistance, CallbackInfoReturnable<Integer> cir) {
        if (ModConfig.get().arthropods.general.noFallDamage && this.getType().isIn(EntityTypeTags.ARTHROPOD)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "applyDamage", at = @At("HEAD"))
    private void healUndeadAttacker(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        if (this.isInvulnerableTo(world, source)) return;

        if (ModConfig.get().undead.general.attackHeal && amount > 0) {
            LivingEntity attacker = source.getAttacker() instanceof LivingEntity
                    ? (LivingEntity) source.getAttacker()
                    : null;

            if (attacker != null
                    && attacker.getType().isIn(EntityTypeTags.UNDEAD)
                    && !(attacker instanceof WitherEntity)) {
                attacker.heal(amount);
            }
        }
    }

    @ModifyVariable(method = "applyMovementInput", at = @At("HEAD"), argsOnly = true)
    private Vec3d updateHorseInput(Vec3d movementInput) {
        if (this.hasControllingPassenger() && this.getFirstPassenger() instanceof MobEntity) {
            if (ModConfig.get().skeletons.skeletonHorse.increasedSpeed
                    && this.getType().equals(EntityType.SKELETON_HORSE)) {
                return movementInput.multiply(1.0, 1.0, 2.0);
            } else if (ModConfig.get().zombies.zombieHorse.increasedSpeed
                    && this.getType().equals(EntityType.ZOMBIE_HORSE)) {
                return movementInput.multiply(1.0, 1.0, 2.5);
            }
        }
        return movementInput;
    }

    @Inject(method = "tickRiding", at = @At("TAIL"))
    protected void onTickRiding(CallbackInfo ci) {

    }

    @Inject(method = "pushAwayFrom", at = @At("TAIL"))
    protected void onPushAwayFrom(Entity entity, CallbackInfo ci) {

    }

    @ModifyReturnValue(method = "isBaby", at = @At("RETURN"))
    protected boolean updateBaby(boolean original) {
        return original;
    }

    @ModifyReturnValue(method = "getScaleFactor", at = @At("RETURN"))
    protected float updateScaleFactor(float original) {
        return original;
    }
}
