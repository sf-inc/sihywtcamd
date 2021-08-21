package com.github.galatynf.sihywtcamd.client;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.entity.CobwebEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class SihywtcamdClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(Sihywtcamd.COBWEB, CobwebEntityRenderer::new);
    }
}
