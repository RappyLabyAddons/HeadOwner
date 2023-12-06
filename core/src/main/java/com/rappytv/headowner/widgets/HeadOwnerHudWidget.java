package com.rappytv.headowner.widgets;

import com.rappytv.headowner.HeadOwnerAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;

public class HeadOwnerHudWidget extends TextHudWidget<TextHudWidgetConfig> {

    private final HeadOwnerAddon addon;
    private String name;
    private TextLine line;

    public HeadOwnerHudWidget(HeadOwnerAddon addon) {
        super("headowner");
        this.addon = addon;
        this.name = Laby.labyAPI().getName();
        this.bindCategory(HudWidgetCategory.INGAME);
    }

    @Override
    public void load(TextHudWidgetConfig config) {
        super.load(config);
        this.line = super.createLine("HeadOwner", name);
    }

    @Override
    public void onTick(boolean isEditorContext) {
        String newName = addon.getSkullApi().getDisplay(addon.configuration());
        if(name != null && name.equals(newName)) return;
        name = newName;

        this.line.updateAndFlush(name != null ? name : labyAPI.getName());
    }

    @Override
    public boolean isVisibleInGame() {
        return name != null && super.isVisibleInGame();
    }
}
