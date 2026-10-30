package com.github.galatynf.sihywtcamd.mixin.piglin;

import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinBrainMixin {
    @Inject(method = "isWearingSafeArmor", at = @At("HEAD"), cancellable = true)
    private static void needFullGoldenArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.get().nether.piglin.goldenArmor > 1) {
            int goldenArmorPiece = 0;

            for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
                if (entity.getItemBySlot(equipmentSlot).is(ItemTags.PIGLIN_SAFE_ARMOR)) {
                    goldenArmorPiece++;
                }
            }

            cir.setReturnValue(goldenArmorPiece >= ModConfig.get().nether.piglin.goldenArmor);
        }
    }
}
