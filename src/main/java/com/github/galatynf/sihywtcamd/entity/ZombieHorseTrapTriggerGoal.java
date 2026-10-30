package com.github.galatynf.sihywtcamd.entity;

import com.github.galatynf.sihywtcamd.imixin.ZombieHorseIMixin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import org.jetbrains.annotations.Nullable;

public class ZombieHorseTrapTriggerGoal extends Goal {
    private final ZombieHorse zombieHorse;

    public ZombieHorseTrapTriggerGoal(ZombieHorse zombieHorse) {
        this.zombieHorse = zombieHorse;
    }

    @Override
    public boolean canUse() {
        return this.zombieHorse.level().hasNearbyAlivePlayer(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ(), 10.0);
    }

    @Override
    public void tick() {
        ServerLevel serverWorld = (ServerLevel)this.zombieHorse.level();
        DifficultyInstance localDifficulty = serverWorld.getCurrentDifficultyAt(this.zombieHorse.blockPosition());
        ((ZombieHorseIMixin) this.zombieHorse).sihywtcamd$setTrapped(false);
        this.zombieHorse.setTamed(true);
        this.zombieHorse.setAge(0);
        this.zombieHorse.setPersistenceRequired();
        LightningBolt lightningEntity = EntityTypes.LIGHTNING_BOLT.create(serverWorld, EntitySpawnReason.TRIGGERED);
        if (lightningEntity == null) {
            return;
        }
        lightningEntity.snapTo(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ());
        lightningEntity.setVisualOnly(true);
        serverWorld.addFreshEntity(lightningEntity);

        // Spawn zombie
        Zombie zombieEntity = EntityTypes.ZOMBIE.create(this.zombieHorse.level(), EntitySpawnReason.TRIGGERED);
        if (zombieEntity == null) return;
        this.initZombie(zombieEntity, localDifficulty, Items.IRON_SWORD, this.zombieHorse);
        EntityUtils.trimEntityArmor(serverWorld, zombieEntity, TrimMaterials.LAPIS, TrimPatterns.SHAPER);
        serverWorld.addFreshEntityWithPassengers(zombieEntity);

        AbstractHorse abstractHorseEntity;
        // Spawn husk
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityTypes.HUSK.create(this.zombieHorse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.IRON_SHOVEL, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, zombieEntity, TrimMaterials.COPPER, TrimPatterns.DUNE);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }
        // Spawn drowned
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityTypes.DROWNED.create(this.zombieHorse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.TRIDENT, abstractHorseEntity);
            EntityUtils.trimEntityArmor(serverWorld, zombieEntity, TrimMaterials.DIAMOND, TrimPatterns.COAST);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }
        // Spawn zombie villager
        if ((abstractHorseEntity = this.getHorse(localDifficulty)) != null
                && (zombieEntity = EntityTypes.ZOMBIE_VILLAGER.create(this.zombieHorse.level(), EntitySpawnReason.TRIGGERED)) != null) {
            this.initZombie(zombieEntity, localDifficulty, Items.IRON_AXE, abstractHorseEntity);
            zombieEntity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            zombieEntity.setDropChance(EquipmentSlot.CHEST, 0.f);
            EntityUtils.enchantEquipment(zombieEntity, EquipmentSlot.CHEST, localDifficulty);
            EntityUtils.trimEntityArmor(serverWorld, zombieEntity, TrimMaterials.EMERALD, TrimPatterns.HOST);
            serverWorld.addFreshEntityWithPassengers(abstractHorseEntity);
        }
    }

    @Nullable
    private AbstractHorse getHorse(DifficultyInstance localDifficulty) {
        ZombieHorse zombieHorseEntity = EntityTypes.ZOMBIE_HORSE.create(this.zombieHorse.level(), EntitySpawnReason.TRIGGERED);
        if (zombieHorseEntity != null) {
            zombieHorseEntity.finalizeSpawn((ServerLevel) this.zombieHorse.level(), localDifficulty, EntitySpawnReason.TRIGGERED, null);
            zombieHorseEntity.setPos(this.zombieHorse.getX(), this.zombieHorse.getY(), this.zombieHorse.getZ());
            zombieHorseEntity.push(this.zombieHorse.getRandom().triangle(0.0, 1.1485), 0.0, this.zombieHorse.getRandom().triangle(0.0, 1.1485));
            zombieHorseEntity.invulnerableTime = 60;
            zombieHorseEntity.setPersistenceRequired();
            zombieHorseEntity.setTamed(true);
            zombieHorseEntity.setAge(0);
            ((ZombieHorseIMixin) zombieHorseEntity).sihywtcamd$setTrapHorse(true);
        }
        return zombieHorseEntity;
    }

    private void initZombie(Zombie zombieEntity, DifficultyInstance localDifficulty, Item item, AbstractHorse vehicle) {
        if (zombieEntity == null) return;

        zombieEntity.finalizeSpawn((ServerLevel) vehicle.level(), localDifficulty, EntitySpawnReason.TRIGGERED,
                new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(zombieEntity.getRandom()), false));
        zombieEntity.setPos(vehicle.getX(), vehicle.getY(), vehicle.getZ());
        zombieEntity.invulnerableTime = 60;
        zombieEntity.setPersistenceRequired();

        zombieEntity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(item));
        if (zombieEntity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            zombieEntity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        EntityUtils.enchantEquipment(zombieEntity, EquipmentSlot.MAINHAND, localDifficulty);
        EntityUtils.enchantEquipment(zombieEntity, EquipmentSlot.HEAD, localDifficulty);

        zombieEntity.startRiding(vehicle);
    }
}
