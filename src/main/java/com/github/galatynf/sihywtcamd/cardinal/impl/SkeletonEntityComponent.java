package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.Sihywtcamd;
import com.github.galatynf.sihywtcamd.cardinal.api.SkeletonEntityComponentAPI;
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
        if (Sihywtcamd.DEBUG && provider instanceof LivingEntity livingEntity) {
            livingEntity.setCustomName(Text.of("Spectral"));
            livingEntity.setCustomNameVisible(true);
        }
    }

    @Override
    public void readData(ReadView tag) {
        this.isSpectral = tag.getBoolean("IsSpectral", true);
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putBoolean("IsSpectral", this.isSpectral);
    }
}
