package me.Szabolcs2008.redstonebridge.SuggestionProvider;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class BridgeNamesProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        for (String name : RedstoneBridge.bridges.listBridges()) {
            if (CommandSource.shouldSuggest(builder.getRemaining(), name)) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }
}
