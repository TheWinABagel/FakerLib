package dev.shadowsoffire.placebo.commands;

import com.google.gson.Gson;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.shadowsoffire.placebo.mixin.getters.MinecraftServerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.LootCommand;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootTable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SerializeLootTableCommand {

    public static final Gson GSON = Deserializers.createLootTableSerializer().setPrettyPrinting().create();

    public static final DynamicCommandExceptionType NOT_FOUND = new DynamicCommandExceptionType(arg -> Component.translatable("fakerlib.cmd.not_found", arg));

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.then(Commands.literal("serialize_loot_table").requires(s -> s.hasPermission(2)).then(Commands.argument("loot_table", ResourceLocationArgument.id()).suggests(LootCommand.SUGGEST_LOOT_TABLE).executes(ctx -> {
            ResourceLocation id = ResourceLocationArgument.getId(ctx, "loot_table");
            LootTable table = ((MinecraftServerAccessor) ctx.getSource().getServer()).getResources().managers().getLootData().getLootTable(id);
            if (table == LootTable.EMPTY) throw NOT_FOUND.create(id);
            String path = "fakerlib_serialized/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json";
            File file = new File(FabricLoader.getInstance().getGameDir().toFile(), path);
            file.getParentFile().mkdirs();
            if (attemptSerialize(table, file)) {
                ctx.getSource().sendSuccess(() -> Component.translatable("fakerlib.cmd.serialize_success", id, path), true);
            }
            else ctx.getSource().sendFailure(Component.translatable("fakerlib.cmd.serialize_failure"));
            return 0;
        })));
    }

    public static boolean attemptSerialize(LootTable table, File file) {
        String json = GSON.toJson(table);
        try (FileWriter w = new FileWriter(file)) {
            w.write(json);
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
