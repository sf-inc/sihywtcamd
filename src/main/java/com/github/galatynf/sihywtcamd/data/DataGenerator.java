package com.github.galatynf.sihywtcamd.data;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(AdvancementsProvider::new);
    }

    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
            AdvancementEntry root = Advancement.Builder.create()
                    .display(
                            Items.STONE_SWORD,
                            Text.translatable("advancements.root.title"),
                            Text.translatable("advancements.root.description"),
                            Identifier.ofVanilla("textures/block/deepslate_top.png"),
                            AdvancementFrame.TASK,
                            true,
                            false,
                            false
                    )
                    .criterion("kill_something", OnKilledCriterion.Conditions.createPlayerKilledEntity())
                    .criterion("killed_by_something", OnKilledCriterion.Conditions.createEntityKilledPlayer())
                    .requirements(AdvancementRequirements.anyOf(List.of("kill_something", "killed_by_something")))
                    .build(consumer, Sihywtcamd.MOD_ID + "/root");

            AdvancementEntry fullGoldenArmor = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.GOLDEN_CHESTPLATE,
                            Text.translatable("advancements.full_golden_armor.title"),
                            Text.translatable("advancements.full_golden_armor.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("full_golden_armor", InventoryChangedCriterion.Conditions.items(
                            Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS))
                    .build(consumer, Sihywtcamd.MOD_ID + "/full_golden_armor");

            AdvancementEntry killIllusioner = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.BOW,
                            Text.translatable("advancements.kill_illusioner.title"),
                            Text.translatable("advancements.kill_illusioner.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("kill_illusioner", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                            EntityPredicate.Builder.create().type(EntityType.ILLUSIONER)))
                    .build(consumer, Sihywtcamd.MOD_ID + "/kill_illusioner");

            AdvancementEntry killPhantomEnd = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.PHANTOM_MEMBRANE,
                            Text.translatable("advancements.kill_phantom_end.title"),
                            Text.translatable("advancements.kill_phantom_end.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("kill_phantom_end", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                            EntityPredicate.Builder.create()
                                    .type(EntityType.PHANTOM)
                                    .location(LocationPredicate.Builder.createBiome(registryLookup
                                            .getWrapperOrThrow(RegistryKeys.BIOME).getOrThrow(BiomeKeys.MEADOW)))))
                    .build(consumer, Sihywtcamd.MOD_ID + "/kill_phantom_end");

            AdvancementEntry babyZombiesTower1 = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.SPYGLASS,
                            Text.translatable("advancements.spyglass_at_baby_1.title"),
                            Text.translatable("advancements.spyglass_at_baby_1.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("spyglass_at_baby_1", UsingItemCriterion.Conditions.create(
                            EntityPredicate.Builder.create()
                                    .typeSpecific(PlayerPredicate.Builder.create()
                                            .lookingAt(EntityPredicate.Builder.create()
                                                    .type(EntityTypeTags.ZOMBIES)
                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                                                    .passenger(EntityPredicate.Builder.create()
                                                            .type(EntityTypeTags.ZOMBIES)
                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true))))
                                            .build()),
                            ItemPredicate.Builder.create().items(Items.SPYGLASS))
                    )
                    .build(consumer, Sihywtcamd.MOD_ID + "/spyglass_at_baby_1");

            AdvancementEntry babyZombiesTower4 = Advancement.Builder.create()
                    .parent(babyZombiesTower1)
                    .display(
                            Items.SPYGLASS,
                            Text.translatable("advancements.spyglass_at_baby_4.title"),
                            Text.translatable("advancements.spyglass_at_baby_4.description"),
                            null,
                            AdvancementFrame.GOAL,
                            true,
                            true,
                            false
                    )
                    .criterion("spyglass_at_baby_4", UsingItemCriterion.Conditions.create(
                            EntityPredicate.Builder.create()
                                    .typeSpecific(PlayerPredicate.Builder.create()
                                            .lookingAt(EntityPredicate.Builder.create()
                                                    .type(EntityTypeTags.ZOMBIES)
                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                                                    .passenger(EntityPredicate.Builder.create()
                                                            .type(EntityTypeTags.ZOMBIES)
                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                                                            .passenger(EntityPredicate.Builder.create()
                                                                    .type(EntityTypeTags.ZOMBIES)
                                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                                                                    .passenger(EntityPredicate.Builder.create()
                                                                            .type(EntityTypeTags.ZOMBIES)
                                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                                                                            .passenger(EntityPredicate.Builder.create()
                                                                                    .type(EntityTypeTags.ZOMBIES)
                                                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true)))))))
                                            .build()),
                            ItemPredicate.Builder.create().items(Items.SPYGLASS))
                    )
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .build(consumer, Sihywtcamd.MOD_ID + "/spyglass_at_baby_4");

            AdvancementEntry rideZombieHorse = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.SADDLE,
                            Text.translatable("advancements.ride_zombie_horse.title"),
                            Text.translatable("advancements.ride_zombie_horse.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("ride_zombie_horse", StartedRidingCriterion.Conditions.create(
                            EntityPredicate.Builder.create()
                                    .vehicle(EntityPredicate.Builder.create()
                                            .type(EntityType.ZOMBIE_HORSE))))
                    .build(consumer, Sihywtcamd.MOD_ID + "/ride_zombie_horse");

            AdvancementEntry convertMagma = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.SLIME_BALL,
                            Text.translatable("advancements.convert_magma.title"),
                            Text.translatable("advancements.convert_magma.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("convert_magma", AdvancementRegistry.createMagmaToSlime())
                    .build(consumer, Sihywtcamd.MOD_ID + "/convert_magma");

            AdvancementEntry convertSlime = Advancement.Builder.create()
                    .parent(convertMagma)
                    .display(
                            Items.MAGMA_CREAM,
                            Text.translatable("advancements.convert_slime.title"),
                            Text.translatable("advancements.convert_slime.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("convert_slime", AdvancementRegistry.createSlimeToMagma())
                    .build(consumer, Sihywtcamd.MOD_ID + "/convert_slime");
        }
    }
}
