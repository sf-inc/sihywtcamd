package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ZombieEntity.class)
public abstract class ZombieMixin extends HostileEntity {
    @Shadow public abstract void setCanBreakDoors(boolean canBreakDoors);

    @Shadow protected abstract boolean shouldBreakDoors();

    protected ZombieMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryAttack", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void stealHealth(Entity target, CallbackInfoReturnable<Boolean> cir, boolean bl) {
        if (bl && ModConfig.get().zombie.general.attackHeal) {
            float damage = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if (target instanceof LivingEntity livingEntity) {
                damage += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), livingEntity.getGroup());
            }
            this.setHealth(Math.min(this.getHealth() + damage, this.getMaxHealth()));
        }
    }

    @Inject(method = "applyAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZombieEntity;initAttributes()V", shift = At.Shift.AFTER), cancellable = true)
    private void applyDifferentAttributes(float chanceMultiplier, CallbackInfo ci) {
        float random = this.random.nextFloat();
        if (ModConfig.get().zombie.general.knockbackResistance) {
            this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).addPersistentModifier(
                    new EntityAttributeModifier("Random zombie bonus", chanceMultiplier * 0.05 + this.random.nextDouble() * 0.05, EntityAttributeModifier.Operation.ADDITION));
        }
        if (ModConfig.get().zombie.general.leaderAndSiege) {
            if (random < 0.05F + 0.05F * chanceMultiplier) {
                this.getAttributeInstance(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).addPersistentModifier(
                        new EntityAttributeModifier("Leader zombie bonus", chanceMultiplier * 0.2 + 0.3, EntityAttributeModifier.Operation.ADDITION));
                this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(
                        new EntityAttributeModifier("Leader zombie bonus", 1.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                this.setHealth(this.getMaxHealth());
                ci.cancel();
            } else if (random < 0.15F + 0.1F * chanceMultiplier) {
                this.setCanBreakDoors(this.shouldBreakDoors());
                ci.cancel();
            }
        }
        if (ModConfig.get().zombie.general.brainless
                && random > 0.8F - (1.F - chanceMultiplier) * 0.05F) {
            this.targetSelector.getGoals().removeIf(goal -> !(goal.getGoal() instanceof RevengeGoal));
            ci.cancel();
        }
        if (ModConfig.get().zombie.general.brainless
                || ModConfig.get().zombie.general.leaderAndSiege) {
            ci.cancel();
        }
    }

    @Inject(method = "initAttributes", at = @At("HEAD"))
    private void increaseSpawnReinforcement(CallbackInfo ci) {
        if (ModConfig.get().zombie.general.knockbackResistance) {
            this.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.35);
        }
    }
}
