package com.github.galatynf.sihywtcamd.data;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
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
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

import java.util.List;
import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(AdvancementsProvider::new);
    }

    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricDataOutput dataGenerator) {
            super(dataGenerator);
        }

        @Override
        public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
            AdvancementEntry root = Advancement.Builder.create()
                    .display(
                            Items.STONE_SWORD,
                            Text.translatable("advancements.root.title"),
                            Text.translatable("advancements.root.description"),
                            new Identifier("textures/block/deepslate_top.png"),
                            AdvancementFrame.TASK,
                            true,
                            false,
                            false
                    )
                    .criterion("kill_something", OnKilledCriterion.Conditions.createPlayerKilledEntity())
                    .criterion("killed_by_something", OnKilledCriterion.Conditions.createEntityKilledPlayer())
                    .requirements(AdvancementRequirements.anyOf(List.of("kill_something", "killed_by_something")))
                    .build(consumer, Sihywtcamd.MOD_ID + "/root");

            AdvancementEntry enterMessyCobweb = Advancement.Builder.create()
                    .parent(root)
                    .display(
                            Items.COBWEB,
                            Text.translatable("advancements.enter_messy_cobweb.title"),
                            Text.translatable("advancements.enter_messy_cobweb.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("enter_messy_cobweb", EnterBlockCriterion.Conditions.block(Sihywtcamd.MESSY_COBWEB))
                    .build(consumer, Sihywtcamd.MOD_ID + "/enter_messy_cobweb");

            AdvancementEntry babySpiderSpawn = Advancement.Builder.create()
                    .parent(enterMessyCobweb)
                    .display(
                            Items.SPIDER_SPAWN_EGG,
                            Text.translatable("advancements.baby_spider_spawn.title"),
                            Text.translatable("advancements.baby_spider_spawn.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("baby_spider_spawn", SummonedEntityCriterion.Conditions.create(
                            EntityPredicate.Builder.create()
                                    .type(EntityType.SPIDER)
                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                    ))
                    .build(consumer, Sihywtcamd.MOD_ID + "/baby_spider_spawn");

            AdvancementEntry babyCaveSpiderSpawn = Advancement.Builder.create()
                    .parent(babySpiderSpawn)
                    .display(
                            Items.CAVE_SPIDER_SPAWN_EGG,
                            Text.translatable("advancements.baby_cave_spider_spawn.title"),
                            Text.translatable("advancements.baby_cave_spider_spawn.description"),
                            null,
                            AdvancementFrame.TASK,
                            true,
                            true,
                            false
                    )
                    .criterion("baby_cave_spider_spawn", SummonedEntityCriterion.Conditions.create(
                            EntityPredicate.Builder.create()
                                    .type(EntityType.CAVE_SPIDER)
                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true))
                    ))
                    .build(consumer, Sihywtcamd.MOD_ID + "/baby_cave_spider_spawn");

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
                                    .location(LocationPredicate.Builder.createBiome(BiomeKeys.THE_END))))
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
        }
    }
}
