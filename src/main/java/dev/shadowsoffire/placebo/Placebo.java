package dev.shadowsoffire.placebo;

import dev.shadowsoffire.placebo.color.GradientColor;
import dev.shadowsoffire.placebo.commands.PlaceboCommand;
import dev.shadowsoffire.placebo.events.ServerEvents;
import dev.shadowsoffire.placebo.json.GearSetRegistry;
import dev.shadowsoffire.placebo.loot.StackLootEntry;
import dev.shadowsoffire.placebo.mixin.getters.TextColorGetter;
import dev.shadowsoffire.placebo.packets.ButtonClickMessage;
import dev.shadowsoffire.placebo.packets.PatreonDisableMessage;
import dev.shadowsoffire.placebo.patreon.TrailsManager;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import dev.shadowsoffire.placebo.tabs.TabFillingRegistry;
import dev.shadowsoffire.placebo.util.PlaceboTaskQueue;
import dev.shadowsoffire.placebo.util.PlaceboUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class Placebo implements ModInitializer {

    public static final String MODID = "fakerlib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        PlaceboTaskQueue.init();
        ServerEvents.init();
        ButtonClickMessage.init();
        PatreonDisableMessage.initServer();
        registerCommands();
        TextColorGetter.setNAMED_COLORS(new HashMap<>(TextColorGetter.getNAMED_COLORS()));
        PlaceboUtil.registerCustomColor(GradientColor.RAINBOW);
        DynamicRegistry.sync();
        GearSetRegistry.INSTANCE.register();
        LOGGER.info("Look at the cleanse, look at the moves!");
        StackLootEntry.poke();
        WingsManager.init();
        TrailsManager.init();
        TabFillingRegistry.fillTabs();
    }

    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PlaceboCommand.register(dispatcher, registryAccess);
        });
    }

}
