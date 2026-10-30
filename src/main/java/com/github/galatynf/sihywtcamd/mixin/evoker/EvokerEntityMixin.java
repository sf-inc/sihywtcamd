package com.github.galatynf.sihywtcamd.mixin.evoker;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Evoker.class)
public abstract class EvokerEntityMixin extends EntityMixin {
    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder increaseHealth(AttributeSupplier.Builder original) {
        if (ModConfig.get().illagers.evoker.increasedHealth) {
            original.add(Attributes.MAX_HEALTH, 36.0);
        }
        return original;
    }

    @Override
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        if (!ModConfig.get().illagers.evoker.stopArrows || !damageSource.is(DamageTypeTags.IS_PROJECTILE)) {
            return original;
        }
        Entity projectile = damageSource.getDirectEntity();
        boolean magic = projectile != null && (projectile.isOnFire()
                || projectile instanceof AbstractArrow persistentProjectile && persistentProjectile.getPierceLevel() > 0);
        return !magic || original;
    }
}
