package com.rappytv.headowner.api;

import com.rappytv.headowner.config.HeadOwnerConfig;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface ISkullApi {

    String getDisplay(HeadOwnerConfig config);
    String getCopy(HeadOwnerConfig config);

}
