package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SkeletonEntityComponentAPI;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SkeletonEntityComponent implements SkeletonEntityComponentAPI {
    private final Object provider;
    private boolean isSpectral = false;

    public SkeletonEntityComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public boolean isSpectral() {
        return this.isSpectral;
    }

    @Override
    public void setSpectral() {
        this.isSpectral = true;
        if (ModConfig.get().general.enableDebugInfo && provider instanceof LivingEntity livingEntity) {
            livingEntity.setCustomName(Component.nullToEmpty("Spectral"));
            livingEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public void readData(ValueInput readView) {
        this.isSpectral = readView.getBooleanOr("IsSpectral", false);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putBoolean("IsSpectral", this.isSpectral);
    }
}
