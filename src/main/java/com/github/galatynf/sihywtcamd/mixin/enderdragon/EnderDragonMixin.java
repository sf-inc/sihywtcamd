package com.github.galatynf.sihywtcamd.mixin.enderdragon;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnderDragonEntity.class)
public class EnderDragonMixin extends MobEntity {
    protected EnderDragonMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickWithEndCrystals", at = @At("HEAD"))
    private void resummonSomeCrystals(CallbackInfo ci) {
        int crystalsToSummon = ModConfig.get().bosses.enderDragon.crystalsToSummon;
        int summonedCrystals = MyComponents.ENDER_DRAGON_COMPONENT.get(this).getNumberOfSummonedCrystals();

        if (!this.getWorld().isClient()
                && crystalsToSummon > 0
                && summonedCrystals < crystalsToSummon) {
            ServerWorld world = (ServerWorld) this.getWorld();
            float healthRatio = (float) (crystalsToSummon - summonedCrystals) / (crystalsToSummon + 1);

            if (this.getHealth() < healthRatio * this.getMaxHealth()) {
                MyComponents.ENDER_DRAGON_COMPONENT.get(this).incrementNumberOfSummonedCrystals();

                EndSpikeFeature.Spike spike = this.getSpikeWithoutCrystal(world);
                if (spike == null) return;

                for (BlockPos blockPos : BlockPos.iterate(new BlockPos(spike.getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(spike.getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10))) {
                    world.removeBlock(blockPos, false);
                }
                world.createExplosion(null, spike.getCenterX() + 0.5, spike.getHeight(), spike.getCenterZ() + 0.5, 5.0f, World.ExplosionSourceType.BLOCK);
                EndSpikeFeatureConfig endSpikeFeatureConfig = new EndSpikeFeatureConfig(false, ImmutableList.of(spike), null);
                Feature.END_SPIKE.generateIfValid(endSpikeFeatureConfig, world, world.getChunkManager().getChunkGenerator(), Random.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
            }
        }
    }

    @Unique
    private EndSpikeFeature.Spike getSpikeWithoutCrystal(ServerWorld world) {
        List<EndSpikeFeature.Spike> spikes = EndSpikeFeature.getSpikes(world);
        if (spikes.isEmpty()) return null;

        int random = this.random.nextInt(spikes.size());
        EndSpikeFeature.Spike spike;
        for (int i = 0; i < spikes.size(); ++i) {
            spike = spikes.get((random + i) % spikes.size());
            if (world.getNonSpectatingEntities(EndCrystalEntity.class, spike.getBoundingBox()).isEmpty()) {
                return spike;
            }
        }

        return null;
    }
}
