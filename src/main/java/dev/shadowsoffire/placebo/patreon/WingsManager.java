package dev.shadowsoffire.placebo.patreon;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.PlaceboClient;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.PatreonUtils.WingType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class WingsManager {

    public static Map<UUID, WingType> WINGS = new HashMap<>();
    public static final Set<UUID> DISABLED = new HashSet<>();

    public static void init() {
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
        }, "Placebo (FakerLib) Patreon Wing Loader").start();
    }

    public static void keys() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (PlaceboClient.TOGGLE_WINGS.consumeClick()) {
                PatreonDisableMessage.sendToServer(new PatreonDisableMessage(1, client.player.getUUID()));
            }
        });
    }

    public static WingType getType(UUID id) {
        return WINGS.get(id);
    }
}
