package com.github.galatynf.sihywtcamd.mixin.evoker;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EvokerEntity.class)
public abstract class EvokerEntityMixin extends EntityMixin {
    @Override
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return ModConfig.get().illagers.evoker.stopArrows
                ? damageSource.isIn(DamageTypeTags.IS_PROJECTILE) || original
                : original;
    }
}
