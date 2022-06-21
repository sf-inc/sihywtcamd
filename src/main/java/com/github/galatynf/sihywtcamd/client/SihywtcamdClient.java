package com.github.galatynf.sihywtcamd.client;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.entity.CobwebEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class SihywtcamdClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Sihywtcamd.COBWEB, CobwebEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Sihywtcamd.MESSY_COBWEB, RenderLayer.getCutout());
    }
}
