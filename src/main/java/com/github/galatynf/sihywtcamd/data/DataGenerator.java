package com.github.galatynf.sihywtcamd.data;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.BiomeKeys;

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
        public void generateAdvancement(Consumer<Advancement> consumer) {
            Advancement root = Advancement.Builder.create()
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
                    .criteriaMerger(CriterionMerger.OR)
                    .build(consumer, Sihywtcamd.MOD_ID + "/root");

            Advancement enterMessyCobweb = Advancement.Builder.create()
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

            Advancement babySpiderSpawn = Advancement.Builder.create()
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
                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                    ))
                    .build(consumer, Sihywtcamd.MOD_ID + "/baby_spider_spawn");

            Advancement babyCaveSpiderSpawn = Advancement.Builder.create()
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
                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                    ))
                    .build(consumer, Sihywtcamd.MOD_ID + "/baby_cave_spider_spawn");

            Advancement fullGoldenArmor = Advancement.Builder.create()
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

            Advancement killIllusioner = Advancement.Builder.create()
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

            Advancement killPhantomEnd = Advancement.Builder.create()
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
                                    .location(LocationPredicate.Builder.create().biome(BiomeKeys.THE_END).build())))
                    .build(consumer, Sihywtcamd.MOD_ID + "/kill_phantom_end");

            Advancement babyZombiesTower1 = Advancement.Builder.create()
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
                                                    .type(EntityType.ZOMBIE)
                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                    .passenger(EntityPredicate.Builder.create()
                                                            .type(EntityType.ZOMBIE)
                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                            .build())
                                                    .build())
                                            .build()),
                            ItemPredicate.Builder.create().items(Items.SPYGLASS))
                    )
                    .build(consumer, Sihywtcamd.MOD_ID + "/spyglass_at_baby_1");

            Advancement babyZombiesTower4 = Advancement.Builder.create()
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
                                                    .type(EntityType.ZOMBIE)
                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                    .passenger(EntityPredicate.Builder.create()
                                                            .type(EntityType.ZOMBIE)
                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                            .passenger(EntityPredicate.Builder.create()
                                                                    .type(EntityType.ZOMBIE)
                                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                                    .passenger(EntityPredicate.Builder.create()
                                                                            .type(EntityType.ZOMBIE)
                                                                            .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                                            .passenger(EntityPredicate.Builder.create()
                                                                                    .type(EntityType.ZOMBIE)
                                                                                    .flags(EntityFlagsPredicate.Builder.create().isBaby(true).build())
                                                                                    .build())
                                                                            .build())
                                                                    .build())
                                                            .build())
                                                    .build())
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
        }
    }
}
