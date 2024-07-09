package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public interface BabyComponentAPI extends Component, AutoSyncedComponent {
    boolean isBaby();
    void setBaby(boolean baby);
}
