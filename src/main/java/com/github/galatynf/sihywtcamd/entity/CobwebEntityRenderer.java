package com.github.galatynf.sihywtcamd.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class CobwebEntityRenderer<T extends CobwebProjectileEntity> extends EntityRenderer<T> {
    private final Identifier TEXTURE = new Identifier("textures/block/cobweb.png");

    public CobwebEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public void render(T cobwebProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, cobwebProjectileEntity.prevYaw, cobwebProjectileEntity.getYaw()) - 90.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, cobwebProjectileEntity.prevPitch, cobwebProjectileEntity.getPitch())));

        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(45.0F));
        matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(cobwebProjectileEntity)));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getModel();
        Matrix3f matrix3f = entry.getNormal();
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, i);
        this.method_23153(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, i);

        for(int u = 0; u < 4; ++u) {
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            this.method_23153(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, i);
            this.method_23153(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, i);
            this.method_23153(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, i);
            this.method_23153(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, i);
        }

        matrixStack.pop();
        super.render(cobwebProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void method_23153(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, int i, int j, int k, float f, float g, int l, int m, int n, int o) {
        vertexConsumer.vertex(matrix4f, (float)i, (float)j, (float)k).color(255, 255, 255, 255).texture(f, g).overlay(OverlayTexture.DEFAULT_UV).light(o).normal(matrix3f, (float)l, (float)n, (float)m).next();
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
