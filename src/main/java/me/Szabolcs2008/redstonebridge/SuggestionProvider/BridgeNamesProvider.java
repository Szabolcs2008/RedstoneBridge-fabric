package me.Szabolcs2008.redstonebridge.SuggestionProvider;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.Szabolcs2008.redstonebridge.Config.Bridges;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BridgeNamesProvider implements SuggestionProvider<ServerCommandSource> {
    public static String ALL = "all";
    public static String ENABLED = "enabled";
    public static String DISABLED = "disabled";

    private final String mode;


    public BridgeNamesProvider(String mode) {
        this.mode = mode;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (String name : RedstoneBridge.bridges.listBridges()) {
            if (mode.equals(ALL)) {
                if (CommandSource.shouldSuggest(builder.getRemaining(), name)) {
                    builder.suggest(name);
                }
            } else if (mode.equals(ENABLED)) {
                if (RedstoneBridge.bridges.isEnabled(name) && CommandSource.shouldSuggest(builder.getRemaining(), name)) {
                    builder.suggest(name);
                }
            } else if (mode.equals(DISABLED)) {
                if (!RedstoneBridge.bridges.isEnabled(name) && CommandSource.shouldSuggest(builder.getRemaining(), name)) {
                    builder.suggest(name);
                }
            } else {
                RedstoneBridge.LOGGER.warn("Invalid provider mode!");
            }

        }
        return builder.buildFuture();
    }
}
