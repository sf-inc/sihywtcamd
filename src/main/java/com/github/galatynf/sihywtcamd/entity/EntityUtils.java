package com.github.galatynf.sihywtcamd.entity;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;

public class EntityUtils {
    public static void trimEntityArmor(ServerWorld world, MobEntity mob, EquipmentSlot slot,
                                       RegistryKey<ArmorTrimMaterial> material, RegistryKey<ArmorTrimPattern> pattern) {
        ItemStack armorStack = mob.getEquippedStack(slot);
        if (armorStack.isEmpty()) return;

        DynamicRegistryManager drm = world.getRegistryManager();
        Registry<ArmorTrimMaterial> trimMaterialRegistry = drm.get(RegistryKeys.TRIM_MATERIAL);
        Registry<ArmorTrimPattern> trimPatternRegistry = drm.get(RegistryKeys.TRIM_PATTERN);

        armorStack.set(DataComponentTypes.TRIM, new ArmorTrim(
                trimMaterialRegistry.entryOf(material), trimPatternRegistry.entryOf(pattern)));
    }
}
