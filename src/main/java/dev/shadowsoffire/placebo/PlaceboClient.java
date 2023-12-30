package dev.shadowsoffire.placebo;

import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.TrailsManager;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import dev.shadowsoffire.placebo.patreon.wings.Wing;
import dev.shadowsoffire.placebo.reload.ReloadListenerPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class PlaceboClient implements ClientModInitializer {

    public static final ModelLayerLocation WING_LOC = new ModelLayerLocation(new ResourceLocation(Placebo.MODID, "wings"), "main");
    public static final KeyMapping TOGGLE_WINGS = KeyBindingHelper.registerKeyBinding(new KeyMapping("fakerlib.toggleWings", GLFW.GLFW_KEY_KP_8, "key.categories.fakerlib"));
    public static final KeyMapping TOGGLE_TRAILS = KeyBindingHelper.registerKeyBinding(new KeyMapping("fakerlib.toggleTrails",GLFW.GLFW_KEY_KP_9, "key.categories.fakerlib"));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticks++;
        });
        ReloadListenerPacket.Start.setup();
        ReloadListenerPacket.Content.setup();
        ReloadListenerPacket.End.setup();
        EntityModelLayerRegistry.registerModelLayer(WING_LOC, Wing::createLayer);
        WingsManager.keys();
        TrailsManager.clientTick();
        TrailsManager.keys();
        PatreonDisableMessage.initClient();
    }

    public static long ticks = 0;

    public static float getColorTicks() {
        return (ticks + Minecraft.getInstance().getDeltaFrameTime()) / 0.5F;
    }
}
