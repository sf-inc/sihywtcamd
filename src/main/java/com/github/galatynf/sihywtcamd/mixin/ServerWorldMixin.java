package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level {
    @Shadow public abstract GameRules getGameRules();
    @Shadow public abstract DifficultyInstance getCurrentDifficultyAt(BlockPos pos);

    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef,
                               RegistryAccess registryManager, Holder<DimensionType> dimensionEntry,
                               boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @ModifyVariable(method = "tickThunder", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LightningBolt;setVisualOnly(Z)V", shift = At.Shift.AFTER))
    private LightningBolt spawnZombieHorse(LightningBolt lightningEntity) {
        if (!ModConfig.get().zombies.zombieHorse.zombieHorseTrap) return lightningEntity;

        BlockPos blockPos = lightningEntity.blockPosition();
        ZombieHorse zombieHorseEntity;
        DifficultyInstance localDifficulty = this.getCurrentDifficultyAt(blockPos);
        boolean canSpawn = !lightningEntity.visualOnly
                && this.getGameRules().get(GameRules.SPAWN_MOBS)
                && this.random.nextDouble() < localDifficulty.getEffectiveDifficulty() * 0.01
                && !this.getBlockState(blockPos.below()).is(BlockTags.LIGHTNING_RODS);
        if (canSpawn && (zombieHorseEntity = EntityType.ZOMBIE_HORSE.create(this, EntitySpawnReason.TRIGGERED)) != null) {
            lightningEntity.setVisualOnly(true);
            ((ZombieHorseIMixin) zombieHorseEntity).sihywtcamd$setTrapped(true);
            ((ZombieHorseIMixin) zombieHorseEntity).sihywtcamd$setTrapHorse(true);
            zombieHorseEntity.setAge(0);
            zombieHorseEntity.setPersistenceRequired();
            zombieHorseEntity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            this.addFreshEntity(zombieHorseEntity);
        }
        return lightningEntity;
    }
}
