package com.github.galatynf.sihywtcamd.mixin.enderdragon;

import com.github.galatynf.sihywtcamd.cardinal.MyComponents;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;

@Mixin(EnderDragon.class)
public class EnderDragonMixin extends Mob {
    protected EnderDragonMixin(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "checkCrystals", at = @At("HEAD"))
    private void resummonSomeCrystals(CallbackInfo ci) {
        int crystalsToSummon = ModConfig.get().bosses.enderDragon.crystalsToSummon;
        int summonedCrystals = MyComponents.ENDER_DRAGON_COMPONENT.get(this).getNumberOfSummonedCrystals();

        if (!this.level().isClientSide()
                && ((EnderDragon) (Object) this).getDragonFight() != null
                && crystalsToSummon > 0
                && summonedCrystals < crystalsToSummon) {
            ServerLevel world = (ServerLevel) this.level();
            float healthRatio = (float) (crystalsToSummon - summonedCrystals) / (crystalsToSummon + 1);

            if (this.tickCount % 20 == 0 && this.getHealth() < healthRatio * this.getMaxHealth()) {
                EndSpikeFeature.EndSpike spike = this.getSpikeWithoutCrystal(world);
                if (spike == null) return;

                MyComponents.ENDER_DRAGON_COMPONENT.get(this).incrementNumberOfSummonedCrystals();

                for (BlockPos blockPos : BlockPos.betweenClosed(new BlockPos(spike.getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(spike.getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10))) {
                    world.removeBlock(blockPos, false);
                }
                world.explode(null, spike.getCenterX() + 0.5, spike.getHeight(), spike.getCenterZ() + 0.5, 5.0f, Level.ExplosionInteraction.BLOCK);
                EndSpikeFeature endSpikeFeature = new EndSpikeFeature(ImmutableList.of(spike), false, Optional.empty());
                endSpikeFeature.place(world, world.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
            }
        }
    }

    @Unique
    private EndSpikeFeature.EndSpike getSpikeWithoutCrystal(ServerLevel world) {
        List<EndSpikeFeature.EndSpike> spikes = EndSpikeFeature.getSpikesForLevel(world);
        if (spikes.isEmpty()) return null;

        int random = this.random.nextInt(spikes.size());
        EndSpikeFeature.EndSpike spike;
        for (int i = 0; i < spikes.size(); ++i) {
            spike = spikes.get((random + i) % spikes.size());
            if (world.getEntitiesOfClass(EndCrystal.class, spike.getTopBoundingBox()).isEmpty()) {
                return spike;
            }
        }

        return null;
    }
}
