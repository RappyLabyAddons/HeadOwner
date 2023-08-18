package com.rappytv.headowner;

import com.rappytv.headowner.config.HeadOwnerConfig;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class HeadOwnerAddon extends LabyAddon<HeadOwnerConfig> {

    @Override
    protected void enable() {
        registerSettingCategory();
    }

    @Override
    protected Class<? extends HeadOwnerConfig> configurationClass() {
        return HeadOwnerConfig.class;
    }
}
