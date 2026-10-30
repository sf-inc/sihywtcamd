package com.github.galatynf.sihywtcamd.mixin.zombie;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Drowned.class)
public abstract class DrownedMixin extends Zombie {
    @Unique
    private static final int TRIDENT_CHANCE = 15;
    @Unique
    private static final float VANILLA_TRIDENT_CHANCE = 0.1F * 10 / 16;

    public DrownedMixin(EntityType<? extends Zombie> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("HEAD"), cancellable = true)
    public void changeProbability(RandomSource random, DifficultyInstance localDifficulty, CallbackInfo ci) {
        if (ModConfig.get().zombies.drowned.tridentSpawn) {
            int rand = random.nextInt(100);
            if (rand < TRIDENT_CHANCE) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else if (rand < 18) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "finalizeSpawn", at = @At(value = "CONSTANT", args = "floatValue=0.5"))
    private float keepVanillaNautilusJockeyChance(float original) {
        if (ModConfig.get().zombies.drowned.tridentSpawn) {
            return original * VANILLA_TRIDENT_CHANCE / (TRIDENT_CHANCE / 100.0F);
        }
        return original;
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void trySpawnAsGuardianJockey(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason,
                                          SpawnGroupData entityData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (ModConfig.get().zombies.drowned.guardianJockeySpawn && this.isBaby() && !this.isPassenger()
                && world.getRandom().nextFloat() < 0.1F + 0.1F * difficulty.getSpecialMultiplier()) {
            List<Guardian> list = world.getEntitiesOfClass(Guardian.class, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D),
                    guardian -> EntitySelector.ENTITY_NOT_BEING_RIDDEN.test(guardian) && !(guardian instanceof ElderGuardian));
            if (!list.isEmpty()) {
                Guardian guardianEntity = list.getFirst();
                this.startRiding(guardianEntity);
            } else {
                Guardian guardianEntity = EntityType.GUARDIAN.create(this.level(), EntitySpawnReason.JOCKEY);
                if (guardianEntity != null) {
                    guardianEntity.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    guardianEntity.finalizeSpawn(world, difficulty, EntitySpawnReason.JOCKEY, null);
                    this.startRiding(guardianEntity);
                    world.addFreshEntity(guardianEntity);
                }
            }
        }
    }

    @ModifyArg(method = "travelInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Drowned;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
    private float increaseVelocity(float speed) {
        return ModConfig.get().zombies.drowned.highVelocity ? 0.1F : speed;
    }
}
