package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.client.render.entity.GhastEntityRenderer;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(GhastEntityRenderer.class)
public abstract class GhastRendererMixin {
    @Unique
    private static final Identifier GHAST_TEXTURE = id("textures/entity/ghast.png");
    @Unique
    private static final Identifier GHAST_SHOOTING_TEXTURE = id("textures/entity/ghast_shooting.png");

    @Inject(method = "getTexture*", at = @At("HEAD"), cancellable = true)
    private void getTexture(GhastEntity ghastEntity, CallbackInfoReturnable<Identifier> cir) {
        if (ModConfig.get().cosmetics.translucentGhast) {
            cir.setReturnValue(ghastEntity.isShooting() ? GHAST_SHOOTING_TEXTURE : GHAST_TEXTURE);
        }
    }
}
