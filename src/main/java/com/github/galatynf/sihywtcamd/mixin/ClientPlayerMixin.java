package com.github.galatynf.sihywtcamd.mixin;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerMixin extends PlayerEntity {
    @Unique
    private boolean sihytcamd_hasTinnitus = false;
    @Unique
    private final HashMap<SoundCategory, Float> sihywtcamd_oldValues = new HashMap<>();

    public ClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTinnitusEffect(CallbackInfo ci) {
        if (!this.sihytcamd_hasTinnitus && this.hasStatusEffect(Sihywtcamd.TINNITUS)) {
            StatusEffectInstance tinnitus = this.getStatusEffect(Sihywtcamd.TINNITUS);
            if (tinnitus == null) return;
            this.sihytcamd_hasTinnitus = true;
            int amplifier = tinnitus.getAmplifier();
            amplifier = Math.min(++amplifier, 100);
            for (SoundCategory soundCategory : SoundCategory.values()) {
                if (!soundCategory.equals(SoundCategory.MASTER)) {
                    float oldValue = MinecraftClient.getInstance().options.getSoundVolume(soundCategory);
                    this.sihywtcamd_oldValues.put(soundCategory, oldValue);
                    if (soundCategory.equals(SoundCategory.AMBIENT) ||soundCategory.equals(SoundCategory.WEATHER)) {
                        MinecraftClient.getInstance().options.setSoundVolume(soundCategory,
                                Math.min(1.0F, oldValue * (1.0F + 0.5F * (amplifier / 100.0F))));
                    } else {
                        MinecraftClient.getInstance().options.setSoundVolume(soundCategory,
                                oldValue * (1.0F - 0.9F * (amplifier / 100.0F)));
                    }
                }
            }
        } else if (this.sihytcamd_hasTinnitus && !this.hasStatusEffect(Sihywtcamd.TINNITUS)) {
            this.sihytcamd_hasTinnitus = false;
            for (SoundCategory soundCategory : SoundCategory.values()) {
                if (!soundCategory.equals(SoundCategory.MASTER)) {
                    Float oldValue = this.sihywtcamd_oldValues.get(soundCategory);
                    if (oldValue != null) {
                        MinecraftClient.getInstance().options.setSoundVolume(soundCategory, oldValue);
                    }
                }
            }
        }
    }
}
