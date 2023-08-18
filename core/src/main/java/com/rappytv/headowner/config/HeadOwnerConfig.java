package com.rappytv.headowner.config;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.KeybindWidget.KeyBindSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class HeadOwnerConfig extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
    @SliderSetting(steps = 1, min = 5, max = 25)
    private final ConfigProperty<Integer> distance = new ConfigProperty<>(10);
    @KeyBindSetting
    private final ConfigProperty<Key> copyKey = new ConfigProperty<>(Key.K);

    @Override
    public ConfigProperty<Boolean> enabled() {
        return enabled;
    }
    public int distance() {
        return distance.get();
    }
    public Key copyKey() {
        return copyKey.get();
    }
}
