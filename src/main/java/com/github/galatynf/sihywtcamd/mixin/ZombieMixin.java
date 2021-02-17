package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieEntity.class)
public abstract class ZombieMixin extends LivingEntity {

    protected ZombieMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private float reduceDamage(DamageSource source, float amount) {
        if(source.getAttacker() instanceof LivingEntity) {
            LivingEntity atker = (LivingEntity)source.getAttacker();
            if(EnchantmentHelper.getLevel(Enchantments.SMITE, atker.getMainHandStack()) > 0)
                return amount;
            }
        if(source.isOutOfWorld()
            || source.isFire()
            || source.isSourceCreativePlayer()) {
            return amount;
        }
        return 1;
    }
}
