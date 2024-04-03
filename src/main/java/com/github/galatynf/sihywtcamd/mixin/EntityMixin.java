package com.github.galatynf.sihywtcamd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    protected boolean updateInvulnerableTo(boolean original, DamageSource damageSource) {
        return original;
    }
}
