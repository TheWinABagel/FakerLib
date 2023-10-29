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

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

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

    public static void init() {
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
        });
    }

    public static void sendToServer(PatreonDisableMessage msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(msg.type);
        buf.writeByte(msg.id == null ? 0 : 1);
        if (msg.id != null) buf.writeUUID(msg.id);
        ClientPlayNetworking.send(ID, buf);
    }

/*
    public static class Provider implements MessageProvider<PatreonDisableMessage> {

        @Override
        public Class<PatreonDisableMessage> getMsgClass() {
            return PatreonDisableMessage.class;
        }

        @Override
        public void write(PatreonDisableMessage msg, FriendlyByteBuf buf) {
            buf.writeByte(msg.type);
            buf.writeByte(msg.id == null ? 0 : 1);
            if (msg.id != null) buf.writeUUID(msg.id);
        }

        @Override
        public PatreonDisableMessage read(FriendlyByteBuf buf) {
            int type = buf.readByte();
            if (buf.readByte() == 1) {
                return new PatreonDisableMessage(type, buf.readUUID());
            }
            else return new PatreonDisableMessage(type);
        }

        @Override
        public void handle(PatreonDisableMessage msg, Supplier<Context> ctx) {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                MessageHelper.handlePacket(() -> {
                    PacketDistro.sendToAll(Placebo.CHANNEL, new PatreonDisableMessage(msg.type, ctx.get().getSender().getUUID()));
                }, ctx);
            }
            else MessageHelper.handlePacket(() -> {
                Set<UUID> set = msg.type == 0 ? TrailsManager.DISABLED : WingsManager.DISABLED;
                if (set.contains(msg.id)) {
                    set.remove(msg.id);
                }
                else set.add(msg.id);
            }, ctx);
        }

    }
*/
}
