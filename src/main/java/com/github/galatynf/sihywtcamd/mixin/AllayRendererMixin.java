package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.client.render.entity.AllayEntityRenderer;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.MOD_ID;

@Mixin(AllayEntityRenderer.class)
public abstract class AllayRendererMixin {
    @Unique
    private static final Identifier ALLAY_TEXTURE = new Identifier(MOD_ID, "textures/entity/allay.png");

    @Inject(method = "getTexture*", at = @At("HEAD"), cancellable = true)
    private void getTexture(AllayEntity allayEntity, CallbackInfoReturnable<Identifier> cir) {
        if (ModConfig.get().cosmetic.translucentAllay) {
            cir.setReturnValue(ALLAY_TEXTURE);
        }
    }
}
