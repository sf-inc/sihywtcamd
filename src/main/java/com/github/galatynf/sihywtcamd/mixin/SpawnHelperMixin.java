package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;

@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin {

    @Unique
    private static final Weighted<MobSpawnSettings.SpawnerData> END_PHANTOMS_SPAWN = new Weighted<>(new MobSpawnSettings.SpawnerData(EntityTypes.PHANTOM, UniformInt.of(2, 3)), 10);

    @ModifyReturnValue(method = "mobsAt", at = @At("RETURN"))
    private static WeightedList<MobSpawnSettings.SpawnerData> addPhantomSpawnInEnd(WeightedList<MobSpawnSettings.SpawnerData> original, ServerLevel world,
                                                                      StructureManager structureAccessor, ChunkGenerator chunkGenerator,
                                                                      MobCategory spawnGroup, BlockPos pos) {
        if (ModConfig.get().undead.phantom.spawnInEndCities
                && shouldUseEndCitySpawns(world, pos, spawnGroup, structureAccessor)) {
            List<Weighted<MobSpawnSettings.SpawnerData>> entries = new ArrayList<>(original.unwrap());
            entries.add(END_PHANTOMS_SPAWN);
            return WeightedList.of(entries);
        }
        return original;
    }

    @Unique
    private static boolean shouldUseEndCitySpawns(ServerLevel world, BlockPos pos, MobCategory spawnGroup, StructureManager structureAccessor) {
        if (spawnGroup != MobCategory.MONSTER) {
            return false;
        }

        if (pos.getY() < 85 || world.dimension() != Level.END) {
            return false;
        }

        Structure structure = structureAccessor.registryAccess().lookupOrThrow(Registries.STRUCTURE)
                .getValue(BuiltinStructures.END_CITY);
        if (structure == null) {
            return false;
        }
        return structureAccessor.getStructureAt(pos, structure).isValid();
    }
}
