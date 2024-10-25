package com.github.galatynf.sihywtcamd.mixin.evoker;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EvokerEntity.class)
public abstract class EvokerEntityMixin extends EntityMixin {
    @ModifyReturnValue(method = "createEvokerAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder increaseHealth(DefaultAttributeContainer.Builder original) {
        if (ModConfig.get().illagers.evoker.increasedHealth) {
            original.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 36.0);
        }
        return original;
    }

    @Override
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return ModConfig.get().illagers.evoker.stopArrows
                ? damageSource.isIn(DamageTypeTags.IS_PROJECTILE) || original
                : original;
    }
}
