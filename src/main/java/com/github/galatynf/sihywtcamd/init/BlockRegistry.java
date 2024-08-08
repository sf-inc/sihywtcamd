package com.github.galatynf.sihywtcamd.init;

import com.github.galatynf.sihywtcamd.block.MessyCobweb;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class BlockRegistry {
    public static Block MESSY_COBWEB;

    public static void init() {
        if (ModConfig.get().arthropods.generalSpiders.webProjectileGoal) {
            MESSY_COBWEB = new MessyCobweb(AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE_GRAY).sounds(BlockSoundGroup.COBWEB).solid().noCollision().requiresTool()
                    .strength(3.0F).pistonBehavior(PistonBehavior.DESTROY));
            Registry.register(Registries.BLOCK, id("messy_cobweb"), MESSY_COBWEB);
        }
    }
}
