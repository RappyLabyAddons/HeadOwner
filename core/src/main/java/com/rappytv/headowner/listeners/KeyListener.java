package com.rappytv.headowner.listeners;

import com.rappytv.headowner.HeadOwnerAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;

public class KeyListener {

    private final HeadOwnerAddon addon;

    public KeyListener(HeadOwnerAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onKey(KeyEvent event) {
        if(event.state() == State.PRESS && event.key() == addon.configuration().copyKey() && !Laby.labyAPI().minecraft().minecraftWindow().isScreenOpened()) {
            Laby.labyAPI().minecraft().setClipboard(addon.getSkullApi().getCopy(addon.configuration()));
            Laby.references().chatExecutor().displayClientMessage(Component.translatable("headowner.messages.copied", NamedTextColor.GREEN));
        }
    }

}
