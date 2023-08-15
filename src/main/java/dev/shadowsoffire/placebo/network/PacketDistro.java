package dev.shadowsoffire.placebo.network;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;


public class PacketDistro {

    /**
     * Sends a packet to all players who are watching a specific chunk.
     */
    public static void sendToTracking(SimpleChannel channel, Object packet, ServerLevel world, BlockPos pos) {
        world.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> {
            channel.sendToClientsTracking((S2CPacket) packet, p);
        });
    }

    /**
     * Sends a packet to a specific player.
     */
    public static void sendTo(SimpleChannel channel, Object packet, Player player) {
        channel.sendToClient((S2CPacket) packet, (ServerPlayer) player);
    }

    /**
     * Sends a packet to all players on the server.
     */
    public static void sendToAll(SimpleChannel channel, Object packet) {
        channel.sendToClientsInCurrentServer((S2CPacket) packet);
    }

}
