package dev.shadowsoffire.placebo;

import dev.shadowsoffire.placebo.reload.ReloadListenerPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class PlaceboClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticks++;
        });
        ReloadListenerPacket.Start.setup();
        ReloadListenerPacket.Content.setup();
        ReloadListenerPacket.End.setup();
    }


    public static long ticks = 0;


    public static float getColorTicks() {
        return (ticks + Minecraft.getInstance().getDeltaFrameTime()) / 0.5F;
    }


}
