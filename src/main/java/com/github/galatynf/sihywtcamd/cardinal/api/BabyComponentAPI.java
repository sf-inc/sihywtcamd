package com.github.galatynf.sihywtcamd.cardinal.api;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

public interface BabyComponentAPI extends Component, AutoSyncedComponent {
    boolean isBaby();
    void setBaby(boolean baby);
}
