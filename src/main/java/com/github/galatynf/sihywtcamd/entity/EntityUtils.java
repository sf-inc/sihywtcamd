package com.github.galatynf.sihywtcamd.entity;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProviders;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;

import java.util.Optional;

public class EntityUtils {
    public static void trimEntityArmor(ServerWorld world, MobEntity mob, EquipmentSlot slot,
                                       RegistryKey<ArmorTrimMaterial> material, RegistryKey<ArmorTrimPattern> pattern) {
        ItemStack armorStack = mob.getEquippedStack(slot);
        if (armorStack.isEmpty()) return;

        DynamicRegistryManager drm = world.getRegistryManager();
        Optional<RegistryEntry.Reference<ArmorTrimMaterial>> trimMaterial = drm.getOptionalEntry(material);
        Optional<RegistryEntry.Reference<ArmorTrimPattern>> trimPattern = drm.getOptionalEntry(pattern);
        if (trimMaterial.isEmpty() || trimPattern.isEmpty()) return;

        armorStack.set(DataComponentTypes.TRIM, new ArmorTrim(trimMaterial.get(), trimPattern.get()));
    }

    public static void trimEntityArmor(ServerWorld world, MobEntity mob, RegistryKey<ArmorTrimMaterial> material,
                                       RegistryKey<ArmorTrimPattern> pattern) {
        trimEntityArmor(world, mob, EquipmentSlot.HEAD, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.CHEST, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.LEGS, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.FEET, material, pattern);
    }

    public static void enchantEquipment(MobEntity entity, EquipmentSlot slot, LocalDifficulty localDifficulty) {
        ItemStack itemStack = entity.getEquippedStack(slot);
        itemStack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        EnchantmentHelper.applyEnchantmentProvider(itemStack, entity.getEntityWorld().getRegistryManager(), EnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, entity.getRandom());
        entity.equipStack(slot, itemStack);
    }
}
