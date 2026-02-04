package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SkeletonEntityComponentAPI;
import com.github.galatynf.sihywtcamd.config.ModConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;

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
            livingEntity.setCustomName(Text.of("Spectral"));
            livingEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public void readData(ReadView readView) {
        this.isSpectral = readView.getBoolean("IsSpectral", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("IsSpectral", this.isSpectral);
    }
}
