package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(PhantomEntityRenderer.class)
public abstract class PhantomRendererMixin {
    @Unique
    private static final Identifier PHANTOM_TEXTURE = id("textures/entity/phantom.png");

    @ModifyReturnValue(method = "Lnet/minecraft/client/render/entity/PhantomEntityRenderer;getTexture(Lnet/minecraft/entity/mob/PhantomEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"))
    private Identifier getTexture(Identifier original, PhantomEntity phantomEntity) {
        return ModConfig.get().cosmetics.translucentPhantom
                ? PHANTOM_TEXTURE
                : original;
    }
}
