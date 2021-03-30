package com.github.galatynf.sihywtcamd.mixin.phantom;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PhantomEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomEntityRenderer.class)
public abstract class PhantomRendererMixin extends MobEntityRenderer<PhantomEntity, PhantomEntityModel<PhantomEntity>> {
    public PhantomRendererMixin(EntityRenderDispatcher entityRenderDispatcher, PhantomEntityModel<PhantomEntity> entityModel, float f) {
        super(entityRenderDispatcher, entityModel, f);
    }

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void getTexture(PhantomEntity phantomEntity, CallbackInfoReturnable<Identifier> cir) {
        cir.setReturnValue(new Identifier("sihywtcamd", "textures/phantom.png"));
    }

    @Override
    public void render(PhantomEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        this.model.handSwingProgress = this.getHandSwingProgress(mobEntity, g);
        this.model.riding = mobEntity.hasVehicle();
        this.model.child = mobEntity.isBaby();

        float h = MathHelper.lerpAngleDegrees(g, mobEntity.prevBodyYaw, mobEntity.bodyYaw);
        float j = MathHelper.lerpAngleDegrees(g, mobEntity.prevHeadYaw, mobEntity.headYaw);
        float k = j - h;
        float o = this.getAnimationProgress(mobEntity, g);

        float m = MathHelper.lerp(g, mobEntity.prevPitch, mobEntity.pitch);
        float p = 0.0F;

        this.setupTransforms(mobEntity, matrixStack, o, h, g);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(mobEntity, matrixStack, g);
        matrixStack.translate(0.0D, -1.5010000467300415D, 0.0D);
        float q = 0.0F;
        if (!mobEntity.hasVehicle() && mobEntity.isAlive()) {
            p = Math.min(MathHelper.lerp(g, mobEntity.lastLimbDistance, mobEntity.limbDistance), 1.0F);
            q = mobEntity.limbAngle - mobEntity.limbDistance * (1.0F - g);
        }

        this.model.animateModel(mobEntity, q, p, g);
        this.model.setAngles(mobEntity, q, p, o, k, m);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(getTexture(mobEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);

        if (!mobEntity.isSpectator()) {
            for (FeatureRenderer<PhantomEntity, PhantomEntityModel<PhantomEntity>> featureRenderer : this.features) {
                featureRenderer.render(matrixStack, vertexConsumerProvider, i, mobEntity, q, p, g, o, k, m);
            }
        }

        matrixStack.pop();

        if (this.hasLabel(mobEntity)) {
            this.renderLabelIfPresent(mobEntity, mobEntity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
        }
    }
}
