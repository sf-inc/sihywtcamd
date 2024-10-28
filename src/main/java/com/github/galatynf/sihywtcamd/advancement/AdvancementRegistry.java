package com.github.galatynf.sihywtcamd.advancement;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.predicate.entity.EntityPredicate;

import java.util.Optional;

public class AdvancementRegistry {
    public static TickCriterion MAGMA_TO_SLIME_CONVERSION = new TickCriterion();
    public static TickCriterion SLIME_TO_MAGMA_CONVERSION = new TickCriterion();
    public static TickCriterion ZOMBIFIED_PIGLIN_BRUTE_COLLISION = new TickCriterion();

    public static void init() {
        Criteria.register(Sihywtcamd.MOD_ID + "/magma_to_slime_conversion", MAGMA_TO_SLIME_CONVERSION);
        Criteria.register(Sihywtcamd.MOD_ID + "/slime_to_magma_conversion", SLIME_TO_MAGMA_CONVERSION);
        Criteria.register(Sihywtcamd.MOD_ID + "/zombified_piglin_brute_collision", ZOMBIFIED_PIGLIN_BRUTE_COLLISION);
    }

    public static AdvancementCriterion<TickCriterion.Conditions> createMagmaToSlime() {
        return MAGMA_TO_SLIME_CONVERSION.create(new TickCriterion.Conditions(Optional.empty()));
    }

    public static AdvancementCriterion<TickCriterion.Conditions> createSlimeToMagma() {
        return SLIME_TO_MAGMA_CONVERSION.create(new TickCriterion.Conditions(Optional.empty()));
    }

    public static AdvancementCriterion<TickCriterion.Conditions> createBruteCollision() {
        return ZOMBIFIED_PIGLIN_BRUTE_COLLISION.create(new TickCriterion.Conditions(Optional.empty()));
    }

    public static AdvancementCriterion<OnKilledCriterion.Conditions> createPlayerKilledEntity(
            EntityPredicate.Builder playerPredicateBuilder, EntityPredicate.Builder killedEntityPredicateBuilder) {
        return Criteria.PLAYER_KILLED_ENTITY
                .create(
                        new OnKilledCriterion.Conditions(
                                Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(playerPredicateBuilder)),
                                Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(killedEntityPredicateBuilder)),
                                Optional.empty()
                        )
                );
    }
}
