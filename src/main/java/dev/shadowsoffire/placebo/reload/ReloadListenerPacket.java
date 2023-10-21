package dev.shadowsoffire.placebo.reload;

import com.mojang.datafixers.util.Either;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.events.ServerEvents;

import dev.shadowsoffire.placebo.codec.CodecProvider;
import dev.shadowsoffire.placebo.reload.DynamicRegistry.SyncManagement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public abstract class ReloadListenerPacket<T extends ReloadListenerPacket<T>> {

    final String path;

    public ReloadListenerPacket(String path) {
        this.path = path;
    }

    public static class Start extends ReloadListenerPacket<Start> {
        public static ResourceLocation ID = new ResourceLocation(Placebo.MODID, "reload_listener_start");
        public Start(String path) {
            super(path);
        }
        public static void sendToAll(String path) {
            if (ServerEvents.getCurrentServer() != null) {
                List<ServerPlayer> list = ServerEvents.getCurrentServer().getPlayerList().getPlayers();
                for (ServerPlayer p : list) {
                    sendTo(p, path);
                }
            }
        }

        public static void init(ServerPlayer player, String path) {
            sendTo(player, path);
        }

        @Environment(EnvType.CLIENT)
        public static void setup(){
            ClientPlayNetworking.registerGlobalReceiver(ID, ((client, handler, buf, responseSender) -> {
                String msg = buf.readUtf(50);
                SyncManagement.initSync(msg);
            }));
        }

        public static void sendTo(ServerPlayer player, String path) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(path, 50);
            ServerPlayNetworking.send(player, ID, buf);
        }

    }

    public static class Content<V extends CodecProvider<? super V>> extends ReloadListenerPacket<Content<V>> {
        public static ResourceLocation ID = new ResourceLocation(Placebo.MODID, "reload_listener_content");
        final ResourceLocation key;
        final Either<V, FriendlyByteBuf> data;

        public Content(String path, ResourceLocation key, V item) {
            super(path);
            this.key = key;
            this.data = Either.left(item);
        }

        private Content(String path, ResourceLocation key, FriendlyByteBuf buf) {
            super(path);
            this.key = key;
            this.data = Either.right(buf);
        }

        private V readItem() {
                FriendlyByteBuf buf = this.data.right().get();
            try {
                return SyncManagement.readItem(path, buf);
            } catch (Exception ex) {
                Placebo.LOGGER.error("Failure when deserializing a dynamic registry object via network: Registry: {}, Object ID: {}", path, key);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                buf.release();
            }
        }

        @Environment(EnvType.CLIENT)
        public static <V> void setup(){
            ClientPlayNetworking.registerGlobalReceiver(ID, ((client, handler, buf, responseSender) -> {
                String path = buf.readUtf(50);
                ResourceLocation key = buf.readResourceLocation();
                var content = new Content<>(path, key, new FriendlyByteBuf(buf.copy()));
                SyncManagement.acceptItem(path, key, content.readItem());
            }));
        }

        public static class Provider<V extends CodecProvider<? super V>> {

            public static <R extends CodecProvider<? super R>> void sendTo(ServerPlayer player, String path, ResourceLocation k, R v) {
                FriendlyByteBuf buf = PacketByteBufs.create();
                buf.writeUtf(path, 50);
                buf.writeResourceLocation(k);
                SyncManagement.writeItem(path, v, buf);
                ServerPlayNetworking.send(player, ID, buf);
                Placebo.LOGGER.info("Sending packet to player {} with id {}, resource location {}", player, ID, k);
            }

            public static <R extends CodecProvider<? super R>> void sendToAll(String path, ResourceLocation k, R v) {
                if (ServerEvents.getCurrentServer() != null) {
                    List<ServerPlayer> list = ServerEvents.getCurrentServer().getPlayerList().getPlayers();
                    for (ServerPlayer p : list) {
                        sendTo(p, path, k, v);
                    }
                }
            }
        }

    }

    public static class End extends ReloadListenerPacket<End> {
        public static ResourceLocation ID = new ResourceLocation(Placebo.MODID, "reload_listener_end");
        public End(String path) {
            super(path);
        }

        public void write(End msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
            }

        public End read(FriendlyByteBuf buf) {
           return new End(buf.readUtf(50));
        }

        public void handle(End msg) {
            SyncManagement.endSync(msg.path);
        }

        @Environment(EnvType.CLIENT)
        public static <V> void setup(){
            ClientPlayNetworking.registerGlobalReceiver(ID, ((client, handler, buf, responseSender) -> {
                String path = buf.readUtf(50);
                SyncManagement.endSync(path);
            }));
        }

        public static void sendToAll(String path) {
            if (ServerEvents.getCurrentServer() != null) {
                List<ServerPlayer> list = ServerEvents.getCurrentServer().getPlayerList().getPlayers();
                for (ServerPlayer p : list) {
                    sendTo(p, path);
                }
            }
        }

        public static void sendTo(ServerPlayer player, String path) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(path, 50);
            ServerPlayNetworking.send(player, ID, buf);
            SyncManagement.endSync(path);
        }

    }
}
