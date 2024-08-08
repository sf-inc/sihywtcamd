package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.entity.CobwebEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import static com.github.galatynf.sihywtcamd.entity.EntityRegistry.COBWEB;

public class EntityRenderRegistry {
    public static void init() {
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            EntityRendererRegistry.register(COBWEB, CobwebEntityRenderer::new);
        }
    }
}
