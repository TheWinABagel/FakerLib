package dev.shadowsoffire.placebo.reload;

import com.mojang.datafixers.util.Either;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.events.ServerEvents;
import dev.shadowsoffire.placebo.json.PSerializer.PSerializable;

import dev.shadowsoffire.placebo.reload.DynamicRegistry.SyncManagement;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.function.Supplier;

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
            ClientPlayNetworking.registerGlobalReceiver(ID, ((client, handler, buf, responseSender) -> {
                String msg = buf.readUtf(50);
                SyncManagement.initSync(msg);
            }));
            sendTo(player, path);
        }

        public static void sendTo(ServerPlayer player, String path) {
            FriendlyByteBuf buf = PacketByteBufs.create();
            buf.writeUtf(path, 50);
            ServerPlayNetworking.send(player, ID, buf);
        }
        /*
            public void write(Start msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
            }

            public Start read(FriendlyByteBuf buf) {
                return new Start(buf.readUtf(50));
            }

            public void handle(Start msg) {
                SyncManagement.initSync(msg.path);
            }
        */
    }

    public static class Content<V extends TypeKeyed & PSerializable<? super V>> extends ReloadListenerPacket<Content<V>> {

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
                return SyncManagement.readItem(path, key, buf);
            }
            catch (Exception ex) {
                Placebo.LOGGER.error("Failure when deserializing a dynamic registry object via network: Registry: {}, Object ID: {}", path, key);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            finally {
                buf.release();
            }
        }


            public void write(Content<V> msg, FriendlyByteBuf buf) {
                buf.writeUtf(msg.path, 50);
                buf.writeResourceLocation(msg.key);
                SyncManagement.writeItem(msg.path, msg.data.left().get(), buf);
            }

            public Content<V> read(FriendlyByteBuf buf) {
                String path = buf.readUtf(50);
                ResourceLocation key = buf.readResourceLocation();
                return new Content<>(path, key, new FriendlyByteBuf(buf.copy()));
            }

            public void handle(Content<V> msg) {
                SyncManagement.acceptItem(msg.path, msg.readItem());

        }
    }

    public static class End extends ReloadListenerPacket<End> {

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
    }
}
