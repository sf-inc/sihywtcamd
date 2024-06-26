package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HuskEntity.class)
public abstract class HuskMixin extends EntityMixin {
    @Override
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return ModConfig.get().zombies.husk.fireResistant
                ? damageSource.isIn(DamageTypeTags.IS_FIRE) || original
                : original;
    }
}
