package dev.shadowsoffire.placebo.util;

import dev.shadowsoffire.placebo.Placebo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.function.BooleanSupplier;

public class PlaceboTaskQueue {

    private static final Queue<Pair<String, BooleanSupplier>> TASKS = new ArrayDeque<>();

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<Pair<String, BooleanSupplier>> it = TASKS.iterator();
            Pair<String, BooleanSupplier> current = null;
            while (it.hasNext()) {
                current = it.next();
                try {
                    if (current.getRight().getAsBoolean()) it.remove();
                }
                catch (Exception ex) {
                    Placebo.LOGGER.error("An exception occurred while running a ticking task with ID {}.  It will be terminated.", current.getLeft());
                    it.remove();
                    ex.printStackTrace();
                }
            }
        });
        ServerWorldEvents.UNLOAD.register((server, world) -> TASKS.clear()); //When server is stopped
        ServerWorldEvents.LOAD.register((server, world) -> TASKS.clear()); //When server is started
    }


    public static void submitTask(String id, BooleanSupplier task) {
        TASKS.add(Pair.of(id, task));
    }

}
