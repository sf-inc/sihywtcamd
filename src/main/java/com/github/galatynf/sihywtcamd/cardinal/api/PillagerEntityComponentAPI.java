package com.github.galatynf.sihywtcamd.cardinal.api;

import org.ladysnake.cca.api.v3.component.Component;

public interface PillagerEntityComponentAPI extends Component {
    boolean hasFireworkRocket();

    void setPillatrooper(boolean isPillatrooper);
    void setFireworkRocket(boolean hasFireworkRocket);
}
