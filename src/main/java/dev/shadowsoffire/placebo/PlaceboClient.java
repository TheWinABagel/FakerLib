package dev.shadowsoffire.placebo;

import dev.shadowsoffire.placebo.color.GradientColor;
import dev.shadowsoffire.placebo.util.PlaceboUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextColor;

@Environment(EnvType.CLIENT)
public class PlaceboClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticks++;
        });
    }



    /**
     * see PlaceboUtil#registerCustomColor(String, TextColor)
     */
    @Deprecated(forRemoval = true)
    public static <T extends TextColor> void registerCustomColor(String id, T color) {
        PlaceboUtil.registerCustomColor(color);
    }

    public static long ticks = 0;


    public static float getColorTicks() {
        return (ticks + Minecraft.getInstance().getDeltaFrameTime()) / 0.5F;
    }


    @Deprecated(forRemoval = true)
    public static class RainbowColor extends GradientColor {

        public RainbowColor() {
            super(GradientColor.RAINBOW_GRADIENT, "rainbow");
        }
    }
}
