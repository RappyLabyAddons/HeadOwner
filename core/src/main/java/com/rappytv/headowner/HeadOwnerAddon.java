package com.rappytv.headowner;

import com.rappytv.headowner.api.ISkullApi;
import com.rappytv.headowner.config.HeadOwnerConfig;
import com.rappytv.headowner.core.generated.DefaultReferenceStorage;
import com.rappytv.headowner.listeners.KeyListener;
import com.rappytv.headowner.widgets.HeadOwnerHudWidget;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class HeadOwnerAddon extends LabyAddon<HeadOwnerConfig> {

    private ISkullApi skullApi;

    @Override
    protected void enable() {
        skullApi = ((DefaultReferenceStorage) this.referenceStorageAccessor()).iSkullApi();
        registerSettingCategory();
        registerListener(new KeyListener(this));
        labyAPI().hudWidgetRegistry().register(new HeadOwnerHudWidget(this));
    }

    @Override
    protected Class<? extends HeadOwnerConfig> configurationClass() {
        return HeadOwnerConfig.class;
    }

    public ISkullApi getSkullApi() {
        return skullApi;
    }
}
