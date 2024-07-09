package com.github.galatynf.sihywtcamd.client;

import com.github.galatynf.sihywtcamd.block.BlockRegistry;
import com.github.galatynf.sihywtcamd.entity.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SihywtcamdClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRegistry.initClient();
        EntityRegistry.initClient();
    }
}
