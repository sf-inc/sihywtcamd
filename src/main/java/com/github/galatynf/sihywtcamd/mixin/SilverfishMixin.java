package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.github.galatynf.sihywtcamd.Sihywtcamd.id;

@Mixin(SilverfishEntity.class)
public abstract class SilverfishMixin extends MobEntityMixin {
    @Unique
    private static final Identifier BONUS_ID = id("random_silverfish_bonus");

    protected SilverfishMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                              EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        if (!ModConfig.get().arthropods.general.larvaeSpeedBonus) return;

        EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
        if (speed != null) {
            double random = 0.25 * world.getRandom().nextDouble();
            double localDifficulty = 0.25 * difficulty.getClampedLocalDifficulty();
            speed.addPersistentModifier(new EntityAttributeModifier(
                    BONUS_ID,  random + localDifficulty, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
    }
}
