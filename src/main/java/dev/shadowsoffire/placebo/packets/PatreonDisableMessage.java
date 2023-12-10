package dev.shadowsoffire.placebo.packets;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.patreon.TrailsManager;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.UUID;

public class PatreonDisableMessage {

    public static ResourceLocation ID = new ResourceLocation(Placebo.MODID, "patreon_disable");
    private int type;
    private UUID id;

    public PatreonDisableMessage(int type) {
        this.type = type;
    }

    public PatreonDisableMessage(int type, UUID id) {
        this(type);
        this.id = id;
    }

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            int type = buf.readByte();
            int id = buf.readByte();
            UUID uuid = buf.readUUID();
            Set<UUID> set = type == 0 ? TrailsManager.DISABLED : WingsManager.DISABLED;
            Component nameOn = (type == 0) ? Component.translatable("fakerlib.toggleTrailsOn") : Component.translatable("fakerlib.toggleWingsOn");
            Component nameOff = (type == 0) ? Component.translatable("fakerlib.toggleTrailsOff") : Component.translatable("fakerlib.toggleWingsOff");
            if (set.contains(uuid)) {
                set.remove(uuid);
                player.displayClientMessage(nameOn, false);
            }
            else {
                set.add(uuid);
                player.displayClientMessage(nameOff, false);
            }
            if (server.isDedicatedServer()) {
                server.getPlayerList().getPlayers().forEach(serverPlayer -> {
                    sendToPlayer(new PatreonDisableMessage(type, uuid), serverPlayer);
                });
            }
        });
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(ID, (client, handler, buf, responseSender) -> {
            int type = buf.readByte();
            int id = buf.readByte();
            UUID uuid = buf.readUUID();
            Set<UUID> set = type == 0 ? TrailsManager.DISABLED : WingsManager.DISABLED;
            if (set.contains(uuid)) {
                set.remove(uuid);
            }
            else {
                set.add(uuid);
            }
        });
    }

    public static void sendToServer(PatreonDisableMessage msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(msg.type);
        buf.writeByte(msg.id == null ? 0 : 1);
        if (msg.id != null) buf.writeUUID(msg.id);
        ClientPlayNetworking.send(ID, buf);
    }

    public static void sendToPlayer(PatreonDisableMessage msg, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(msg.type);
        buf.writeByte(msg.id == null ? 0 : 1);
        if (msg.id != null) buf.writeUUID(msg.id);
        ServerPlayNetworking.send(player, ID, buf);
    }
}
