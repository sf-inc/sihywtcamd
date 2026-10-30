package com.github.galatynf.sihywtcamd.mixin.ghast;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Ghast.class)
public class GhastMixin {
    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder increaseHealth(AttributeSupplier.Builder original) {
        if (ModConfig.get().nether.ghast.increasedHealth) {
            original.add(Attributes.MAX_HEALTH, 30.0);
        }
        return original;
    }
}
