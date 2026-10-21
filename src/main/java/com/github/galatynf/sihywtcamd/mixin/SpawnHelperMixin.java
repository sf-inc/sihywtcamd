package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
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

import java.util.ArrayList;
import java.util.List;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {

    @Unique
    private static final Weighted<SpawnSettings.SpawnEntry> END_PHANTOMS_SPAWN = new Weighted<>(new SpawnSettings.SpawnEntry(EntityType.PHANTOM, 2, 3), 10);

    @ModifyReturnValue(method = "getSpawnEntries", at = @At("RETURN"))
    private static Pool<SpawnSettings.SpawnEntry> addPhantomSpawnInEnd(Pool<SpawnSettings.SpawnEntry> original, ServerWorld world,
                                                                      StructureAccessor structureAccessor, ChunkGenerator chunkGenerator,
                                                                      SpawnGroup spawnGroup, BlockPos pos, @Nullable RegistryEntry<Biome> biomeEntry) {
        if (ModConfig.get().undead.phantom.spawnInEndCities
                && shouldUseEndCitySpawns(world, pos, spawnGroup, structureAccessor)) {
            List<Weighted<SpawnSettings.SpawnEntry>> entries = new ArrayList<>(original.getEntries());
            entries.add(END_PHANTOMS_SPAWN);
            return Pool.of(entries);
        }
        return original;
    }

    @Unique
    private static boolean shouldUseEndCitySpawns(ServerWorld world, BlockPos pos, SpawnGroup spawnGroup, StructureAccessor structureAccessor) {
        if (spawnGroup != SpawnGroup.MONSTER) {
            return false;
        }

        if (pos.getY() < 85 || world.getRegistryKey() != World.END) {
            return false;
        }

        Structure structure = structureAccessor.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE)
                .get(StructureKeys.END_CITY);
        if (structure == null) {
            return false;
        }
        return structureAccessor.getStructureAt(pos, structure).hasChildren();
    }
}
