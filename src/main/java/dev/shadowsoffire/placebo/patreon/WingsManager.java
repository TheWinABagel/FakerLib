package dev.shadowsoffire.placebo.patreon;

import com.mojang.blaze3d.platform.InputConstants;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.PatreonUtils.WingType;
import dev.shadowsoffire.placebo.patreon.wings.Wing;
import dev.shadowsoffire.placebo.patreon.wings.WingLayer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class WingsManager {

    public static Map<UUID, WingType> WINGS = new HashMap<>();
    public static final KeyMapping TOGGLE = KeyBindingHelper.registerKeyBinding(new KeyMapping("fakerlib.toggleWings", GLFW.GLFW_KEY_KP_8, "key.categories.fakerlib"));
    public static final Set<UUID> DISABLED = new HashSet<>();
    public static final ModelLayerLocation WING_LOC = new ModelLayerLocation(new ResourceLocation(Placebo.MODID, "wings"), "main");

    public static void init() {
        EntityModelLayerRegistry.registerModelLayer(WING_LOC, Wing::createLayer);
        new Thread(() -> {
            Placebo.LOGGER.info("Loading patreon wing data...");
            try {
                URL url = new URL("https://raw.githubusercontent.com/TheWinABagel/FakerLib/master/TestWings.txt");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String s;
                    while ((s = reader.readLine()) != null) {
                        String[] split = s.split(" ", 2);
                        if (split.length != 2) {
                            Placebo.LOGGER.error("Invalid patreon wing entry {} will be ignored.", s);
                            continue;
                        }
                        WINGS.put(UUID.fromString(split[0]), WingType.valueOf(split[1]));
                    }
                    reader.close();
                }
                catch (IOException ex) {
                    Placebo.LOGGER.error("Exception loading patreon wing data!");
                    ex.printStackTrace();
                }
            }
            catch (Exception k) {
                // not possible
            }
            Placebo.LOGGER.info("Loaded {} patreon wings.", WINGS.size());
        //    if (WINGS.size() > 0) MinecraftForge.EVENT_BUS.register(WingsManager.class);
        }, "Placebo (FakerLib) Patreon Wing Loader").start();
        keys();
    }

    public static void keys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE.consumeClick()) {
                //to server
                PatreonDisableMessage.sendToServer(new PatreonDisableMessage(1, client.player.getUUID()));
            }
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void addLayers(EntityRendererProvider.Context context, Map<String, EntityRenderer<? extends Player>> skins) {
        Wing.INSTANCE = new Wing(context.getModelSet().bakeLayer(WING_LOC));
        for (String s : skins.keySet()) {
            LivingEntityRenderer skin = (LivingEntityRenderer) skins.get(s);
            skin.addLayer(new WingLayer(skin));
        }
    }

    public static WingType getType(UUID id) {
        return WINGS.get(id);
    }

}
