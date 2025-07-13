package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SlimeEntityComponentAPI;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public class SlimeEntityComponent implements SlimeEntityComponentAPI {
    private int mergeDelay = 50;

    @Override
    public boolean canMerge() {
        return this.mergeDelay == 0;
    }

    @Override
    public boolean hasMerged() {
        return this.mergeDelay < 0;
    }

    @Override
    public void setMerged() {
        this.mergeDelay = -1;
    }

    @Override
    public void updateMerged() {
        if (this.mergeDelay > 0) {
            this.mergeDelay--;
        }
    }

    @Override
    public void readData(ReadView tag) {
        this.mergeDelay = tag.getInt("MergeDelay", 50);
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("MergeDelay", this.mergeDelay);
    }
}
