package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.AllayEntityRenderer;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(AllayEntityRenderer.class)
public abstract class AllayRendererMixin {
    @Unique
    private static final Identifier ALLAY_TEXTURE = id("textures/entity/allay.png");

    @ModifyReturnValue(method = "Lnet/minecraft/client/render/entity/AllayEntityRenderer;getTexture(Lnet/minecraft/entity/passive/AllayEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"))
    private Identifier getTexture(Identifier original, AllayEntity allayEntity) {
        return ModConfig.get().cosmetics.translucentAllay
                ? ALLAY_TEXTURE
                : original;
    }
}
