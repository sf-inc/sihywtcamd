package com.github.galatynf.sihywtcamd.data;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.advancement.AdvancementRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.DamagePredicate;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityHurtPlayerTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.KilledTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.advancements.criterion.PlayerPredicate;
import net.minecraft.advancements.criterion.StartRidingTrigger;
import net.minecraft.advancements.criterion.UsingItemTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(AdvancementsProvider::new);
    }

    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
            HolderGetter<Biome> biomeLookup = registryLookup.lookupOrThrow(Registries.BIOME);
            HolderGetter<EntityType<?>> entityTypeLookup = registryLookup.lookupOrThrow(Registries.ENTITY_TYPE);
            HolderGetter<Item> itemLookup = registryLookup.lookupOrThrow(Registries.ITEM);

            AdvancementHolder fullGoldenArmor = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("nether/distract_piglin"))
                    .display(
                            Items.GOLDEN_CHESTPLATE,
                            Component.translatable("advancements.full_golden_armor.title"),
                            Component.translatable("advancements.full_golden_armor.description"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion("full_golden_armor", InventoryChangeTrigger.TriggerInstance.hasItems(
                            Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS))
                    .save(consumer, Sihywtcamd.MOD_ID + "/full_golden_armor");

            AdvancementHolder bruteCollision = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("nether/root"))
                    .display(
                            Items.GOLDEN_AXE,
                            Component.translatable("advancements.zombified_piglin_brute_collision.title"),
                            Component.translatable("advancements.zombified_piglin_brute_collision.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            true
                    )
                    .addCriterion("zombified_piglin_brute_collision", AdvancementRegistry.createBruteCollision())
                    .rewards(AdvancementRewards.Builder.experience(100))
                    .save(consumer, Sihywtcamd.MOD_ID + "/zombified_piglin_brute_collision");

            AdvancementHolder deflectFireworkRocket = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("story/deflect_arrow"))
                    .display(
                            Items.FIREWORK_ROCKET,
                            Component.translatable("advancements.deflect_firework_rocket.title"),
                            Component.translatable("advancements.deflect_firework_rocket.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("deflected_firework_rocket", EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(
                                    DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType()
                                                    .direct(EntityPredicate.Builder.entity()
                                                            .entityType(EntityTypePredicate.of(
                                                                    entityTypeLookup,
                                                                    EntityType.FIREWORK_ROCKET)))
                                                    .source(EntityPredicate.Builder.entity()
                                                            .entityType(EntityTypePredicate.of(
                                                                    entityTypeLookup,
                                                                    EntityType.PILLAGER))))
                                            .blocked(true)
                            ))
                    .save(consumer, Sihywtcamd.MOD_ID + "/deflect_firework_rocket");

            AdvancementHolder killIllusioner = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("adventure/kill_a_mob"))
                    .display(
                            new ItemStackTemplate(Items.POTION, DataComponentPatch.builder()
                                    .set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.INVISIBILITY))
                                    .build()),
                            Component.translatable("advancements.kill_illusioner.title"),
                            Component.translatable("advancements.kill_illusioner.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("kill_illusioner", AdvancementRegistry.createPlayerKilledEntity(
                            EntityPredicate.Builder.entity().effects(
                                    MobEffectsPredicate.Builder.effects().and(MobEffects.INVISIBILITY)),
                            EntityPredicate.Builder.entity().entityType(
                                    EntityTypePredicate.of(entityTypeLookup, EntityType.ILLUSIONER))))
                    .rewards(AdvancementRewards.Builder.experience(50))
                    .save(consumer, Sihywtcamd.MOD_ID + "/kill_illusioner");

            AdvancementHolder killPhantomEnd = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("end/root"))
                    .display(
                            Items.PHANTOM_MEMBRANE,
                            Component.translatable("advancements.kill_phantom_end.title"),
                            Component.translatable("advancements.kill_phantom_end.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("kill_phantom_end", KilledTrigger.TriggerInstance.playerKilledEntity(
                            EntityPredicate.Builder.entity()
                                    .entityType(EntityTypePredicate.of(entityTypeLookup, EntityType.PHANTOM))
                                    .located(LocationPredicate.Builder.inBiome(biomeLookup.getOrThrow(Biomes.THE_END)))))
                    .save(consumer, Sihywtcamd.MOD_ID + "/kill_phantom_end");

            AdvancementHolder babyZombiesTower1 = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("adventure/spyglass_at_parrot"))
                    .display(
                            Items.SPYGLASS,
                            Component.translatable("advancements.spyglass_at_baby_1.title"),
                            Component.translatable("advancements.spyglass_at_baby_1.description"),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("spyglass_at_baby_1", UsingItemTrigger.TriggerInstance.lookingAt(
                            EntityPredicate.Builder.entity()
                                    .subPredicate(PlayerPredicate.Builder.player()
                                            .setLookingAt(babyZombie(entityTypeLookup)
                                                    .passenger(babyZombie(entityTypeLookup)))
                                            .build()),
                            ItemPredicate.Builder.item().of(itemLookup, Items.SPYGLASS))
                    )
                    .save(consumer, Sihywtcamd.MOD_ID + "/spyglass_at_baby_1");

            AdvancementHolder babyZombiesTower4 = Advancement.Builder.advancement()
                    .parent(babyZombiesTower1)
                    .display(
                            Items.SPYGLASS,
                            Component.translatable("advancements.spyglass_at_baby_4.title"),
                            Component.translatable("advancements.spyglass_at_baby_4.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("spyglass_at_baby_4", UsingItemTrigger.TriggerInstance.lookingAt(
                            EntityPredicate.Builder.entity()
                                    .subPredicate(PlayerPredicate.Builder.player()
                                            .setLookingAt(babyZombie(entityTypeLookup)
                                                    .passenger(babyZombie(entityTypeLookup)
                                                            .passenger(babyZombie(entityTypeLookup)
                                                                    .passenger(babyZombie(entityTypeLookup)
                                                                            .passenger(babyZombie(entityTypeLookup))))))
                                            .build()),
                            ItemPredicate.Builder.item().of(itemLookup, Items.SPYGLASS))
                    )
                    .rewards(AdvancementRewards.Builder.experience(25))
                    .save(consumer, Sihywtcamd.MOD_ID + "/spyglass_at_baby_4");

            AdvancementHolder rideZombieHorse = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("adventure/root"))
                    .display(
                            Items.SADDLE,
                            Component.translatable("advancements.ride_zombie_horse.title"),
                            Component.translatable("advancements.ride_zombie_horse.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            true
                    )
                    .addCriterion("ride_zombie_horse", StartRidingTrigger.TriggerInstance.playerStartsRiding(
                            EntityPredicate.Builder.entity()
                                    .vehicle(EntityPredicate.Builder.entity()
                                            .entityType(EntityTypePredicate.of(entityTypeLookup, EntityType.ZOMBIE_HORSE)))))
                    .rewards(AdvancementRewards.Builder.experience(10))
                    .save(consumer, Sihywtcamd.MOD_ID + "/ride_zombie_horse");

            AdvancementHolder convertMagma = Advancement.Builder.advancement()
                    .parent(Identifier.withDefaultNamespace("nether/root"))
                    .display(
                            Items.SLIME_BALL,
                            Component.translatable("advancements.convert_magma.title"),
                            Component.translatable("advancements.convert_magma.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("convert_magma", AdvancementRegistry.createMagmaToSlime())
                    .rewards(AdvancementRewards.Builder.experience(20))
                    .save(consumer, Sihywtcamd.MOD_ID + "/convert_magma");

            AdvancementHolder convertSlime = Advancement.Builder.advancement()
                    .parent(convertMagma)
                    .display(
                            Items.MAGMA_CREAM,
                            Component.translatable("advancements.convert_slime.title"),
                            Component.translatable("advancements.convert_slime.description"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .addCriterion("convert_slime", AdvancementRegistry.createSlimeToMagma())
                    .rewards(AdvancementRewards.Builder.experience(30))
                    .save(consumer, Sihywtcamd.MOD_ID + "/convert_slime");
        }

        private static EntityPredicate.Builder babyZombie(HolderGetter<EntityType<?>> entityTypeLookup) {
            return EntityPredicate.Builder.entity()
                    .entityType(EntityTypePredicate.of(entityTypeLookup, EntityTypeTags.ZOMBIES))
                    .flags(EntityFlagsPredicate.Builder.flags().setIsBaby(true));
        }
    }
}
