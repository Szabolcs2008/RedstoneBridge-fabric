package me.Szabolcs2008.redstonebridge.SuggestionProvider;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.Szabolcs2008.redstonebridge.Config.Bridges;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class ModesProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        String[] modes = {Bridges.RGB, Bridges.ANALOGUE, Bridges.DIGITAL};
        for (String mode : modes) {
            if (CommandSource.shouldSuggest(builder.getRemaining(), mode)) {
                builder.suggest(mode);
            }
        }
        return builder.buildFuture();
    }
}
