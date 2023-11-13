package com.github.galatynf.sihywtcamd.mixin.phantom;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.MOD_ID;

@Mixin(PhantomEntityRenderer.class)
public abstract class PhantomRendererMixin {
    @Unique
    private static final Identifier PHANTOM_TEXTURE = new Identifier(MOD_ID, "textures/entity/phantom.png");

    @Inject(method = "getTexture*", at = @At("HEAD"), cancellable = true)
    private void getTexture(PhantomEntity phantomEntity, CallbackInfoReturnable<Identifier> cir) {
        if (ModConfig.get().cosmetics.translucentPhantom) {
            cir.setReturnValue(PHANTOM_TEXTURE);
        }
    }
}
