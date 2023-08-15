package dev.shadowsoffire.placebo.packets;

import dev.shadowsoffire.placebo.Placebo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.function.Supplier;

/**
 * Allows for easy implementations of client->server button presses. Sends an integer that allows for arbitrary data encoding schemes within the integer
 * space.<br>
 * The Container must implement {@link IButtonContainer}.<br>
 * Defer to using using {@link MultiPlayerGameMode#handleInventoryButtonClick} and {@link AbstractContainerMenu#clickMenuButton} when the buttonId can be a
 * byte.
 */
public class ButtonClickMessage {
    public static ResourceLocation ID = new ResourceLocation(Placebo.MODID, "button_click");
    int button;

    public ButtonClickMessage(int button) {
        this.button = button;
    }

    public static interface IButtonContainer {
        void onButtonClick(int id);
    }

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            int button = buf.readInt();
            if (player.containerMenu instanceof IButtonContainer) {
                ((IButtonContainer) player.containerMenu).onButtonClick(button);
            }
        });
    }

    public static void sendToServer(int ench) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeInt(ench);
        ClientPlayNetworking.send(ID, buf);
    }

}
