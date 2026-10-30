package com.github.galatynf.sihywtcamd.advancement;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.KilledTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class AdvancementRegistry {
    public static PlayerTrigger MAGMA_TO_SLIME_CONVERSION = new PlayerTrigger();
    public static PlayerTrigger SLIME_TO_MAGMA_CONVERSION = new PlayerTrigger();
    public static PlayerTrigger ZOMBIFIED_PIGLIN_BRUTE_COLLISION = new PlayerTrigger();

    public static void init() {
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, Sihywtcamd.MOD_ID + "/magma_to_slime_conversion", MAGMA_TO_SLIME_CONVERSION);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, Sihywtcamd.MOD_ID + "/slime_to_magma_conversion", SLIME_TO_MAGMA_CONVERSION);
        Registry.register(BuiltInRegistries.TRIGGER_TYPES, Sihywtcamd.MOD_ID + "/zombified_piglin_brute_collision", ZOMBIFIED_PIGLIN_BRUTE_COLLISION);
    }

    public static Criterion<PlayerTrigger.TriggerInstance> createMagmaToSlime() {
        return MAGMA_TO_SLIME_CONVERSION.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> createSlimeToMagma() {
        return SLIME_TO_MAGMA_CONVERSION.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<PlayerTrigger.TriggerInstance> createBruteCollision() {
        return ZOMBIFIED_PIGLIN_BRUTE_COLLISION.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()));
    }

    public static Criterion<KilledTrigger.TriggerInstance> createPlayerKilledEntity(
            EntityPredicate.Builder playerPredicateBuilder, EntityPredicate.Builder killedEntityPredicateBuilder) {
        return CriteriaTriggers.PLAYER_KILLED_ENTITY
                .createCriterion(
                        new KilledTrigger.TriggerInstance(
                                Optional.of(EntityPredicate.wrap(playerPredicateBuilder)),
                                Optional.of(EntityPredicate.wrap(killedEntityPredicateBuilder)),
                                Optional.empty()
                        )
                );
    }
}
