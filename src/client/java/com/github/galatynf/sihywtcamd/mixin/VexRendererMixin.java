package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.VexEntityRenderer;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(VexEntityRenderer.class)
public abstract class VexRendererMixin {
    @Unique
    private static final Identifier VEX_TEXTURE = id("textures/entity/vex.png");
    @Unique
    private static final Identifier VEX_CHARGING_TEXTURE = id("textures/entity/vex_charging.png");

    @ModifyReturnValue(method = "Lnet/minecraft/client/render/entity/VexEntityRenderer;getTexture(Lnet/minecraft/entity/mob/VexEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"))
    private Identifier getTexture(Identifier original, VexEntity vexEntity) {
        return ModConfig.get().cosmetics.translucentVex
                ? (vexEntity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE)
                : original;
    }
}
