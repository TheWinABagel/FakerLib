package dev.shadowsoffire.placebo.events;

import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.List;

public class ReloadableServerEvent {
    public static List<PreparableReloadListener> list = new java.util.ArrayList<>();
    public static void addListeners(PreparableReloadListener listeners){
        list.add(listeners);
    }
}
