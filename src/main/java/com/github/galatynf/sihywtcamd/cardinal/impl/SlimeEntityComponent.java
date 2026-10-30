package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.SlimeEntityComponentAPI;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    public void readData(ValueInput readView) {
        this.mergeDelay = readView.getIntOr("MergeDelay", 50);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("MergeDelay", this.mergeDelay);
    }
}
