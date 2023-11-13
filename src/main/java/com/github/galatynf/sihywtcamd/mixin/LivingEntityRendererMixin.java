package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>
        extends EntityRenderer<T> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "getRenderLayer", at = @At("HEAD"), cancellable = true)
    private void setTranslucentPhantoms(T entity, boolean showBody, boolean translucent, boolean showOutline,
                                        CallbackInfoReturnable<@Nullable RenderLayer> cir) {
        if ((entity.getType().equals(EntityType.GHAST) && ModConfig.get().cosmetics.translucentGhast)
                || (entity.getType().equals(EntityType.PHANTOM) && ModConfig.get().cosmetics.translucentPhantom)) {
            cir.setReturnValue(RenderLayer.getEntityTranslucent(getTexture(entity)));
        }
    }
}
