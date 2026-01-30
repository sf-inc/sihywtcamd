package com.github.galatynf.sihywtcamd.cardinal.impl;

import com.github.galatynf.sihywtcamd.cardinal.api.PillagerEntityComponentAPI;
import com.github.galatynf.sihywtcamd.imixin.PillatrooperIMixin;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public class PillagerEntityComponent implements PillagerEntityComponentAPI {
    private final Object provider;
    private boolean isPillatrooper = false;
    private boolean hasFireworkRocket = false;

    public PillagerEntityComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public void setPillatrooper(boolean isPillatrooper) {
        this.isPillatrooper = isPillatrooper;
        if (this.provider instanceof PillatrooperIMixin pillatrooper) {
            pillatrooper.sihywtcamd$setPillatrooper(isPillatrooper);
        }
    }

    @Override
    public boolean hasFireworkRocket() {
        return this.hasFireworkRocket;
    }

    @Override
    public void setFireworkRocket(boolean hasFireworkRocket) {
        this.hasFireworkRocket = hasFireworkRocket;
    }

    @Override
    public void readData(ReadView readView) {
        this.setPillatrooper(readView.getBoolean("IsPillatrooper", false));
        this.setFireworkRocket(readView.getBoolean("HasFireworkRocket", false));
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("IsPillatrooper", this.isPillatrooper);
        writeView.putBoolean("HasFireworkRocket", this.hasFireworkRocket);
    }
}
