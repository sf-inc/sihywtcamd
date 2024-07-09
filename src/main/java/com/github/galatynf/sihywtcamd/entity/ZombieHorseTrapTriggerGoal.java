package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.provider.EnchantmentProviders;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import org.jetbrains.annotations.Nullable;

public class ZombieHorseTrapTriggerGoal extends Goal {
    private final ZombieHorseEntity zombieHorse;

    public ZombieHorseTrapTriggerGoal(ZombieHorseEntity zombieHorse) {
        this.zombieHorse = zombieHorse;
    }

    @Override
    public boolean canStart() {
        return this.zombieHorse.getWorld().isPlayerInRange(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ(), 10.0);
    }

    @Override
    public void tick() {
        ServerWorld serverWorld = (ServerWorld)this.zombieHorse.getWorld();
        LocalDifficulty localDifficulty = serverWorld.getLocalDifficulty(this.zombieHorse.getBlockPos());
        ((ZombieHorseIMixin) this.zombieHorse).sihywtcamd$setTrapped(false);
        this.zombieHorse.setTame(true);
        this.zombieHorse.setBreedingAge(0);
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(serverWorld);
        if (lightningEntity == null) {
            return;
        }
        lightningEntity.refreshPositionAfterTeleport(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ());
        lightningEntity.setCosmetic(true);
        serverWorld.spawnEntity(lightningEntity);

        // Spawn zombie
        ZombieEntity zombieEntity = EntityType.ZOMBIE.create(this.zombieHorse.getWorld());
        if (zombieEntity == null) return;
        this.initZombie(zombieEntity, localDifficulty, Items.IRON_SWORD, this.zombieHorse);
        serverWorld.spawnEntityAndPassengers(zombieEntity);

        AbstractHorseEntity abstractHorseEntity;
        // Spawn husk
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityType.HUSK.create(this.zombieHorse.getWorld())) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.IRON_SHOVEL, abstractHorseEntity);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn drowned
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityType.DROWNED.create(this.zombieHorse.getWorld())) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.TRIDENT, abstractHorseEntity);
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
        // Spawn zombified piglin
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityType.ZOMBIFIED_PIGLIN.create(this.zombieHorse.getWorld())) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.GOLDEN_SWORD, abstractHorseEntity);
            zombieEntity.setTarget(serverWorld.getClosestPlayer(this.zombieHorse, 10.0));
            serverWorld.spawnEntityAndPassengers(abstractHorseEntity);
        }
    }

    @Nullable
    private AbstractHorseEntity getHorse(LocalDifficulty localDifficulty) {
        ZombieHorseEntity zombieHorseEntity = EntityType.ZOMBIE_HORSE.create(this.zombieHorse.getWorld());
        if (zombieHorseEntity != null) {
            zombieHorseEntity.initialize((ServerWorld) this.zombieHorse.getWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
            zombieHorseEntity.setPosition(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ());
            zombieHorseEntity.addVelocity(this.zombieHorse.getRandom().nextTriangular(0.0, 1.1485), 0.0, this.zombieHorse.getRandom().nextTriangular(0.0, 1.1485));
            zombieHorseEntity.timeUntilRegen = 60;
            zombieHorseEntity.setPersistent();
            zombieHorseEntity.setTame(true);
            zombieHorseEntity.setBreedingAge(0);
        }
        return zombieHorseEntity;
    }

    private void initZombie(ZombieEntity zombieEntity, LocalDifficulty localDifficulty, Item item, AbstractHorseEntity vehicle) {
        if (zombieEntity == null) return;

        zombieEntity.initialize((ServerWorld) vehicle.getWorld(), localDifficulty, SpawnReason.TRIGGERED, null);
        zombieEntity.setPosition(vehicle.getX(), vehicle.getY(), vehicle.getZ());
        zombieEntity.timeUntilRegen = 60;
        zombieEntity.setPersistent();

        zombieEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(item));
        if (zombieEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
            zombieEntity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        this.enchantEquipment(zombieEntity, EquipmentSlot.MAINHAND, localDifficulty);
        this.enchantEquipment(zombieEntity, EquipmentSlot.HEAD, localDifficulty);

        zombieEntity.startRiding(vehicle);
    }

    private void enchantEquipment(ZombieEntity rider, EquipmentSlot slot, LocalDifficulty localDifficulty) {
        ItemStack itemStack = rider.getEquippedStack(slot);
        itemStack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        EnchantmentHelper.applyEnchantmentProvider(itemStack, rider.getWorld().getRegistryManager(), EnchantmentProviders.MOB_SPAWN_EQUIPMENT, localDifficulty, rider.getRandom());
        rider.equipStack(slot, itemStack);
    }
}
