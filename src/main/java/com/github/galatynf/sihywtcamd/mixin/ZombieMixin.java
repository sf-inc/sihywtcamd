package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class ZombieMixin extends Entity {

    public ZombieMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private float reduceDamage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (this.getType().equals(EntityType.ZOMBIE)
                && attacker instanceof LivingEntity
                && EnchantmentHelper.getLevel(Enchantments.SMITE, ((LivingEntity) attacker).getMainHandStack()) < 1) {
            return 1;
        }
        return amount;
    }
}
