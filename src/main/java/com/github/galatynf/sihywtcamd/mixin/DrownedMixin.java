package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrownedEntity.class)
public abstract class DrownedMixin extends ZombieEntity {
    @Shadow protected abstract boolean isTargetingUnderwater();

    public DrownedMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void initEquipment(LocalDifficulty difficulty) {
        int rand = this.random.nextInt(100);
        if (rand < 15) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
        } else if (rand < 18 ) {
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() && this.isTouchingWater() && this.isTargetingUnderwater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));
        } else {
            super.travel(movementInput);
        }
    }
}
