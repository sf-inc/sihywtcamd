package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.mixin.EntityMixin;
import net.minecraft.world.entity.monster.zombie.Husk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Husk.class)
public abstract class HuskMixin extends EntityMixin {
    @Override
    protected boolean updateImmunityToFire(boolean original) {
        return ModConfig.get().zombies.husk.fireResistant || original;
    }
}
