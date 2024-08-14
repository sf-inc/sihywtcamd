package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>
        extends EntityRenderer<T> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyReturnValue(method = "getRenderLayer", at = @At("RETURN"))
    private @Nullable RenderLayer setTranslucentPhantoms(@Nullable RenderLayer original, T entity, boolean showBody,
                                                         boolean translucent, boolean showOutline) {
        if ((entity.getType().equals(EntityType.GHAST) && ModConfig.get().cosmetics.translucentGhast)
                || (entity.getType().equals(EntityType.PHANTOM) && ModConfig.get().cosmetics.translucentPhantom)) {
            return RenderLayer.getEntityTranslucent(getTexture(entity));
        }
        return original;
    }
}
