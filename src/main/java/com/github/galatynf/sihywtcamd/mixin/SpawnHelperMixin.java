package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

    @Unique
    private static final Pool<SpawnSettings.SpawnEntry> END_PHANTOMS_SPAWNS = Pool.of(new SpawnSettings.SpawnEntry(EntityType.PHANTOM, 10, 1, 1));

    @Inject(method = "getSpawnEntries", at = @At("HEAD"), cancellable = true)
    private static void addPhantomSpawnInEnd(ServerWorld world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
                                               SpawnGroup spawnGroup, BlockPos pos, @Nullable RegistryEntry<Biome> biomeEntry,
                                               CallbackInfoReturnable<Pool<SpawnSettings.SpawnEntry>> cir) {
        if (ModConfig.get().undead.phantom.spawnInEndCities
                && shouldUseEndCitySpawns(pos, world, spawnGroup, structureAccessor)) {
            cir.setReturnValue(END_PHANTOMS_SPAWNS);
        }
    }

    @Unique
    private static boolean shouldUseEndCitySpawns(BlockPos pos, ServerWorld world, SpawnGroup spawnGroup, StructureAccessor structureAccessor) {
        if (spawnGroup != SpawnGroup.MONSTER) {
            return false;
        }
        if (!world.getBlockState(pos).isOf(Blocks.END_ROD) && !world.getBlockState(pos.north()).isOf(Blocks.END_ROD)) {
            return false;
        }
        Structure structure = structureAccessor.getRegistryManager().get(RegistryKeys.STRUCTURE).get(StructureKeys.END_CITY);
        if (structure == null) {
            return false;
        }
        return structureAccessor.getStructureAt(pos, structure).hasChildren();
    }
}
