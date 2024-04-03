package com.github.galatynf.sihywtcamd.cardinal.api;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface SlimeEntityComponentAPI extends Component {
    boolean hasMerged();
    void setMerged(boolean hasMerged);
}
