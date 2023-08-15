package dev.shadowsoffire.placebo.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;


public class ServerEvents {
    private static MinecraftServer current;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(listener -> {
            current = listener;

        });

        ServerLifecycleEvents.SERVER_STOPPED.register(listener -> {
            current = null;
        });

    }

    @Nullable
    public static MinecraftServer getCurrentServer() {
        return current;
    }
}
