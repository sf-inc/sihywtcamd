package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;

public interface WitherEntityComponentAPI extends Component {
    boolean wasHalfHealthReached();
    void setHalfHealthReached();
}
