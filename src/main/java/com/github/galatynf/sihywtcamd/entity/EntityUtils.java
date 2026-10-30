package com.github.galatynf.sihywtcamd.entity;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;

public class EntityUtils {
    public static void trimEntityArmor(ServerLevel world, Mob mob, EquipmentSlot slot,
                                       ResourceKey<TrimMaterial> material, ResourceKey<TrimPattern> pattern) {
        ItemStack armorStack = mob.getItemBySlot(slot);
        if (armorStack.isEmpty()) return;

        RegistryAccess drm = world.registryAccess();
        Optional<Holder.Reference<TrimMaterial>> trimMaterial = drm.get(material);
        Optional<Holder.Reference<TrimPattern>> trimPattern = drm.get(pattern);
        if (trimMaterial.isEmpty() || trimPattern.isEmpty()) return;

        armorStack.set(DataComponents.TRIM, new ArmorTrim(trimMaterial.get(), trimPattern.get()));
    }

    public static void trimEntityArmor(ServerLevel world, Mob mob, ResourceKey<TrimMaterial> material,
                                       ResourceKey<TrimPattern> pattern) {
        trimEntityArmor(world, mob, EquipmentSlot.HEAD, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.CHEST, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.LEGS, material, pattern);
        trimEntityArmor(world, mob, EquipmentSlot.FEET, material, pattern);
    }

    public static void enchantEquipment(Mob entity, EquipmentSlot slot, DifficultyInstance localDifficulty) {
        ItemStack itemStack = entity.getItemBySlot(slot);
        if (itemStack.isEmpty()) return;

        itemStack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        EnchantmentHelper.enchantItemFromProvider(itemStack, entity.level().registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, entity.getRandom());
        entity.setItemSlot(slot, itemStack);
    }
}
