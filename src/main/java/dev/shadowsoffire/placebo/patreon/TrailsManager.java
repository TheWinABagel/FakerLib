package dev.shadowsoffire.placebo.patreon;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.PlaceboClient;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.PatreonUtils.PatreonParticleType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class TrailsManager {

    public static Map<UUID, PatreonParticleType> TRAILS = new HashMap<>();
    public static final Set<UUID> DISABLED = new HashSet<>();

    public static void init() {
        new Thread(() -> {
            Placebo.LOGGER.info("Loading patreon trails data...");
            try {
                URL url = new URL("https://raw.githubusercontent.com/TheWinABagel/FakerLib/master/TestTrails.txt");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String s;
                    while ((s = reader.readLine()) != null) {
                        String[] split = s.split(" ", 2);
                        if (split.length != 2) {
                            Placebo.LOGGER.error("Invalid patreon trail entry {} will be ignored.", s);
                            continue;
                        }
                        TRAILS.put(UUID.fromString(split[0]), PatreonParticleType.valueOf(split[1]));
                    }
                    reader.close();
                }
                catch (IOException ex) {
                    Placebo.LOGGER.error("Exception loading patreon trails data!");
                    ex.printStackTrace();
                }
            }
            catch (Exception k) {
                // not possible
            }
            Placebo.LOGGER.info("Loaded {} patreon trails.", TRAILS.size());
        }, "Placebo (FakerLib) Patreon Trail Loader").start();
    }

    public static void clientTick() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PatreonParticleType t = null;
            if (Minecraft.getInstance().level != null) {
                for (Player player : Minecraft.getInstance().level.players()) {
                    if (!player.isInvisible() && player.tickCount * 3 % 2 == 0 && !DISABLED.contains(player.getUUID()) && (t = TRAILS.get(player.getUUID())) != null) {
                        ClientLevel world = (ClientLevel) player.level();
                        RandomSource rand = world.random;
                        ParticleOptions type = t.type.get();
                        world.addParticle(type, player.getX() + rand.nextDouble() * 0.4 - 0.2, player.getY() + 0.1, player.getZ() + rand.nextDouble() * 0.4 - 0.2, 0, 0, 0);
                    }
                }
            }
        });

    }

    public static void keys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PlaceboClient.TOGGLE_TRAILS.consumeClick()) {
                PatreonDisableMessage.sendToServer(new PatreonDisableMessage(0, client.player.getUUID()));
            }
        });
    }
}
