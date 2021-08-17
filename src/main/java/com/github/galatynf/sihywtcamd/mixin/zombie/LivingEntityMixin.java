package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.Utils;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(method = "damage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
    private float reduceDamage(DamageSource source, float amount) {
        Entity attacker = source.getAttacker();
        if (Utils.isZombieTypeBuffed(this.getType())
                && !source.bypassesArmor()
                && !source.isExplosive()
                && attacker instanceof LivingEntity
                && !(attacker instanceof IronGolemEntity)
                && !this.isOnFire()
                && EnchantmentHelper.getLevel(Enchantments.SMITE, ((LivingEntity) attacker).getMainHandStack()) < 1) {
            amount = attacker instanceof PlayerEntity
                    ? amount * (ModConfig.get().overworld.zombies.zombiePlayersAttack / 100.0F)
                    : amount * (ModConfig.get().overworld.zombies.zombieMobsAttack / 100.0F);
        }
        return amount;
    }
}
