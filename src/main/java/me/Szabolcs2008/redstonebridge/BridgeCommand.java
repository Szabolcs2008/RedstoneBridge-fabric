package me.Szabolcs2008.redstonebridge;

import com.fasterxml.jackson.databind.JsonNode;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.Szabolcs2008.redstonebridge.Config.Config;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;

import me.Szabolcs2008.redstonebridge.SuggestionProvider.*;

public class BridgeCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("bridge")
                .requires(source -> source.hasPermissionLevel(Config.getConfig().get("required-permission-level").asInt()))
                .executes(BridgeCommand::sendHelp)
                .then(CommandManager.literal("help")
                        .executes(BridgeCommand::sendHelp))
                .then(CommandManager.literal("list")
                        .executes(BridgeCommand::listBridges))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .then(CommandManager.argument("mode", StringArgumentType.string()).suggests(new ModesProvider())
                                        .then(CommandManager.argument("location", BlockPosArgumentType.blockPos())
                                                .executes(BridgeCommand::addBridge)))))
                .then(CommandManager.literal("seturl")
                        .then(CommandManager.argument("name", StringArgumentType.string()).suggests(new BridgeNamesProvider())
                                .then(CommandManager.argument("url", StringArgumentType.string())
                                        .executes(BridgeCommand::setUrl))))
                .then(CommandManager.literal("move")
                        .then(CommandManager.argument("name", StringArgumentType.string()).suggests(new BridgeNamesProvider())
                                .then(CommandManager.argument("location", BlockPosArgumentType.blockPos())
                                        .executes(BridgeCommand::moveBridge))))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.argument("name", StringArgumentType.string()).suggests(new BridgeNamesProvider())
                                .executes(BridgeCommand::removeBridge))));
    }

    private static int sendHelp(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("                     §c§lRedstoneBridge                      "), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §chelp §r| §7shows this help"), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §clist §r| §7Lists all active bridges"), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §cadd §4<name> <mode> <location> §r| §7Maps a redstone component."), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §cseturl §4<name> <url> §r| §7Sets the URL of the redstone component."), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §cmove §4<name> <location> §r| §7Moves the checked block."), false);
        source.sendFeedback(() -> Text.literal(" §f/bridge §cremove §4<name> §r| §7Deletes a mapped redstone component."), false);
        return 1;
    }

    private static int listBridges(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendFeedback(() -> Text.literal("§7Active bridges:"), false);

        for (Iterator<Map.Entry<String, JsonNode>> it = RedstoneBridge.bridges.getBridgesObject().fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> item = it.next();
            source.sendFeedback(() -> Text.literal(" §c§n"+item.getKey()), false);
            source.sendFeedback(() -> Text.literal(" §8| §7Position: §4"+item.getValue().get("block-x")+" §a"+item.getValue().get("block-y")+" §9"+item.getValue().get("block-z")), false);
            source.sendFeedback(() -> Text.literal(" §8| §7Mode: §b"+item.getValue().get("mode").asText()), false);
            source.sendFeedback(() -> Text.literal(" §8| §7URL: §o"+item.getValue().get("url")), false);
        }
        return 1;
    }

    private static int addBridge(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        String mode = StringArgumentType.getString(context, "mode");
        BlockPos location = BlockPosArgumentType.getBlockPos(context, "location");
        ServerCommandSource source = context.getSource();

        if (!RedstoneBridge.bridges.listBridges().contains(name)){
            RedstoneBridge.bridges.addBridge(name, mode, location);
            RedstoneBridge.bridges.saveBridges();

            source.sendFeedback(() -> Text.literal("§7Added §f§o" + name + " §7(pos: " + location.getX() + " " + location.getY() + " " + location.getZ() + ", mode: " + mode + ")"), true);
        } else {
            source.sendFeedback(() -> Text.literal("§7Failed to add bridge §f§o"+name+"§7: §7already exists!"), true);

        }
        return 1;
    }

    private static int setUrl(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String url = StringArgumentType.getString(context, "url");

        ServerCommandSource source = context.getSource();

        RedstoneBridge.bridges.setURL(name, url);
        RedstoneBridge.bridges.saveBridges();

        source.sendFeedback(() -> Text.literal("§7Updated URL for §f§o" + name), true);
        return 1;
    }

    private static int moveBridge(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        BlockPos newLocation = BlockPosArgumentType.getBlockPos(context, "location");

        ServerCommandSource source = context.getSource();

        RedstoneBridge.bridges.moveBridge(name, newLocation);
        RedstoneBridge.bridges.saveBridges();

    source.sendFeedback(() -> Text.literal("§7Moved §f§o" + name + "§7 to " + newLocation.getX() + " " + newLocation.getY() + " " + newLocation.getZ()), true);

        return 1;
    }

    private static int removeBridge(CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();

        RedstoneBridge.bridges.removeBridge(name);
        RedstoneBridge.bridges.saveBridges();

        source.sendFeedback(() -> Text.literal("§7Removed §f§o" + name), true);
        RedstoneBridge.lastUpdateStorage.remove(name);
        return 1;
    }
}
