package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.imixin.ZombieIMixin;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

public class ZombieAttributes {
    public static final Identifier CALLER_BONUS_ID = id("caller_zombie_bonus");
    public static final Identifier RUNNER_BONUS_ID = id("runner_zombie_bonus");
    public static final Identifier TANK_BONUS_ID = id("tank_zombie_bonus");
    public static final Identifier UNSTOPPABLE_BONUS_ID = id("unstoppable_zombie_bonus");

    // Called on the new zombie of a zombie to zombie conversion (drowning, husk to zombie), before vanilla rolls its attributes
    public static void copyOnConversion(Zombie from, Zombie to) {
        copyBaseValue(from, to, Attributes.KNOCKBACK_RESISTANCE);
        copyBaseValue(from, to, Attributes.SPAWN_REINFORCEMENTS_CHANCE);
        copyModifier(from, to, Attributes.SPAWN_REINFORCEMENTS_CHANCE, CALLER_BONUS_ID);
        copyModifier(from, to, Attributes.MOVEMENT_SPEED, RUNNER_BONUS_ID);
        copyModifier(from, to, Attributes.KNOCKBACK_RESISTANCE, UNSTOPPABLE_BONUS_ID);
        if (copyModifier(from, to, Attributes.MAX_HEALTH, TANK_BONUS_ID)) {
            to.setCanBreakDoors(true);
            to.setHealth(to.getMaxHealth());
        }
        ((ZombieIMixin) to).sihywtcamd$setInheritsAttributes(true);
    }

    private static void copyBaseValue(Zombie from, Zombie to, Holder<Attribute> attribute) {
        AttributeInstance fromInstance = from.getAttribute(attribute);
        AttributeInstance toInstance = to.getAttribute(attribute);
        if (fromInstance == null || toInstance == null) return;

        toInstance.setBaseValue(fromInstance.getBaseValue());
    }

    private static boolean copyModifier(Zombie from, Zombie to, Holder<Attribute> attribute, Identifier id) {
        AttributeInstance fromInstance = from.getAttribute(attribute);
        AttributeInstance toInstance = to.getAttribute(attribute);
        AttributeModifier modifier = fromInstance == null ? null : fromInstance.getModifier(id);
        if (modifier == null || toInstance == null) return false;

        toInstance.addOrReplacePermanentModifier(modifier);
        return true;
    }
}
