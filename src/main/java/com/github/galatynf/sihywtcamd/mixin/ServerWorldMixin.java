package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @ModifyVariable(method = "tickChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;setCosmetic(Z)V", shift = At.Shift.AFTER))
    private LightningEntity spawnZombieHorse(LightningEntity lightningEntity) {
        if (!ModConfig.get().zombies.zombieHorse.zombieHorseTrap) return lightningEntity;

        BlockPos blockPos = lightningEntity.getBlockPos();
        ZombieHorseEntity zombieHorseEntity;
        LocalDifficulty localDifficulty = this.getLocalDifficulty(blockPos);
        boolean canSpawn = !lightningEntity.cosmetic
                && this.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)
                && this.random.nextDouble() < localDifficulty.getLocalDifficulty() * 0.01
                && !this.getBlockState(blockPos.down()).isOf(Blocks.LIGHTNING_ROD);
        if (canSpawn && (zombieHorseEntity = EntityType.ZOMBIE_HORSE.create(this)) != null) {
            lightningEntity.setCosmetic(true);
            ((ZombieHorseIMixin) zombieHorseEntity).sihywtcamd$setTrapped(true);
            zombieHorseEntity.setBreedingAge(0);
            zombieHorseEntity.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.spawnEntity(zombieHorseEntity);
        }
        return lightningEntity;
    }
}
