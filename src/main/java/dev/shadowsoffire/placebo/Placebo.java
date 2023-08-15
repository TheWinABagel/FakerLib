package dev.shadowsoffire.placebo;

import dev.shadowsoffire.placebo.color.GradientColor;
import dev.shadowsoffire.placebo.commands.PlaceboCommand;
import dev.shadowsoffire.placebo.events.ServerEvents;
import dev.shadowsoffire.placebo.packets.ButtonClickMessage;
import dev.shadowsoffire.placebo.reload.ReloadListenerPacket;
import dev.shadowsoffire.placebo.util.PlaceboTaskQueue;
import dev.shadowsoffire.placebo.util.PlaceboUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.network.chat.TextColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@SuppressWarnings("deprecation")
public class Placebo implements ModInitializer {

    public static final String MODID = "placebont";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Override
    public void onInitialize() {
        //TextColor.NAMED_COLORS = new HashMap<>(TextColor.NAMED_COLORS);
        PlaceboTaskQueue.init();
        ServerEvents.init();
        ButtonClickMessage.init();
        registerCommands();
        PlaceboUtil.registerCustomColor(GradientColor.RAINBOW);
    //    MessageHelper.registerMessage(CHANNEL, 3, new ReloadListenerPacket.Content<>());
    //    MessageHelper.registerMessage(CHANNEL, 4, new ReloadListenerPacket.End());
    }


    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PlaceboCommand.register(dispatcher);
        });
    }


}
