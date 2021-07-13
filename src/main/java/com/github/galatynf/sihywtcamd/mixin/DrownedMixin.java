package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public abstract class DrownedMixin extends ZombieEntity {
    public DrownedMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    public void changeProbability(LocalDifficulty difficulty, CallbackInfo ci) {
        if (ModConfig.get().overworld.drownedTridentSpawn) {
            int rand = this.random.nextInt(100);
            if (rand < 15) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
            } else if (rand < 18) {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
            ci.cancel();
        }
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/DrownedEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
    private float increaseVelocity(float speed) {
        return ModConfig.get().overworld.drownedHighVelocity ? 0.1F : speed;
    }
}
