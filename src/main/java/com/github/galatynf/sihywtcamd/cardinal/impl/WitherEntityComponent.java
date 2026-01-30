package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.WitherEntityComponentAPI;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public class WitherEntityComponent implements WitherEntityComponentAPI {
    private boolean halfHealthReached = false;

    @Override
    public boolean wasHalfHealthReached() {
        return this.halfHealthReached;
    }

    @Override
    public void setHalfHealthReached() {
        this.halfHealthReached = true;
    }

    @Override
    public void readData(ReadView readView) {
        this.halfHealthReached = readView.getBoolean("HalfHealthReached", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("HalfHealthReached", this.halfHealthReached);
    }
}
