package com.github.galatynf.sihywtcamd.mixin.ghast;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.GhastEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GhastEntity.class)
public class GhastMixin {
    @ModifyReturnValue(method = "createGhastAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder increaseHealth(DefaultAttributeContainer.Builder original) {
        if (ModConfig.get().nether.ghast.increasedHealth) {
            original.add(EntityAttributes.MAX_HEALTH, 30.0);
        }
        return original;
    }
}
