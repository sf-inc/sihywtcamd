package com.github.galatynf.sihywtcamd;

import com.github.galatynf.sihywtcamd.init.BlockRenderRegistry;
import com.github.galatynf.sihywtcamd.init.EntityRenderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SihywtcamdClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderRegistry.init();
        EntityRenderRegistry.init();
    }
}
