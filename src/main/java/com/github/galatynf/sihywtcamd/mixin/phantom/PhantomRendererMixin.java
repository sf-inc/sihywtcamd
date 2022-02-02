package com.github.galatynf.sihywtcamd.mixin.phantom;

import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomEntityRenderer.class)
public abstract class PhantomRendererMixin {
    @Inject(method = "getTexture*", at = @At("HEAD"), cancellable = true)
    private void getTexture(PhantomEntity phantomEntity, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(new Identifier("sihywtcamd", "textures/entity/phantom.png"));
    }
}
