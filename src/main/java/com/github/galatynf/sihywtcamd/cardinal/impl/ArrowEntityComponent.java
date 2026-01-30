package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.ArrowEntityComponentAPI;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public class ArrowEntityComponent implements ArrowEntityComponentAPI {
    private boolean isFrozen = false;

    @Override
    public boolean isFrozen() {
        return this.isFrozen;
    }

    @Override
    public void setFrozen() {
        this.isFrozen = true;
    }

    @Override
    public void readData(ReadView readView) {
        this.isFrozen = readView.getBoolean ("IsFrozen", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("IsFrozen", this.isFrozen);
    }
}
