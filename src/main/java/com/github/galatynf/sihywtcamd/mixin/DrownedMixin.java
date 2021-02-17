package com.github.galatynf.sihywtcamd.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DrownedEntity.class)
public class DrownedMixin extends ZombieEntity {
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
}
