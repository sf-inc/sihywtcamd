package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

import static com.github.galatynf.sihywtcamd.init.BlockRegistry.MESSY_COBWEB;

public class BlockRenderRegistry {
    public static void init() {
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            BlockRenderLayerMap.INSTANCE.putBlock(MESSY_COBWEB, RenderLayer.getCutout());
        }
    }
}
