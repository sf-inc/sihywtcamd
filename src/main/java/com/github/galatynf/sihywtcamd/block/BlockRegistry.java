package com.github.galatynf.sihywtcamd.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class BlockRegistry {
    public static final Block MESSY_COBWEB = new MessyCobweb(AbstractBlock.Settings.create()
            .mapColor(MapColor.WHITE_GRAY).sounds(BlockSoundGroup.COBWEB).solid().noCollision().requiresTool()
            .strength(3.0F).pistonBehavior(PistonBehavior.DESTROY));

    public static void init() {
        Registry.register(Registries.BLOCK, id("messy_cobweb"), MESSY_COBWEB);
    }

    public static void initClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(MESSY_COBWEB, RenderLayer.getCutout());
    }
}
