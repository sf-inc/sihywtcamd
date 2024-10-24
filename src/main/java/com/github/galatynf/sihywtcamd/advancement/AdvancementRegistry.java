package com.github.galatynf.sihywtcamd.advancement;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.TickCriterion;

import java.util.Optional;

public class AdvancementRegistry {
    public static TickCriterion MAGMA_TO_SLIME_CONVERSION = new TickCriterion();
    public static TickCriterion SLIME_TO_MAGMA_CONVERSION = new TickCriterion();

    public static void init() {
        Criteria.register(Sihywtcamd.MOD_ID + "/magma_to_slime_conversion", MAGMA_TO_SLIME_CONVERSION);
        Criteria.register(Sihywtcamd.MOD_ID + "/slime_to_magma_conversion", SLIME_TO_MAGMA_CONVERSION);
    }

    public static AdvancementCriterion<TickCriterion.Conditions> createMagmaToSlime() {
        return MAGMA_TO_SLIME_CONVERSION.create(new TickCriterion.Conditions(Optional.empty()));
    }

    public static AdvancementCriterion<TickCriterion.Conditions> createSlimeToMagma() {
        return SLIME_TO_MAGMA_CONVERSION.create(new TickCriterion.Conditions(Optional.empty()));
    }
}
