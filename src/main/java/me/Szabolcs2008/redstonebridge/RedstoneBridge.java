package me.Szabolcs2008.redstonebridge;

import me.Szabolcs2008.redstonebridge.Config.Bridges;
import me.Szabolcs2008.redstonebridge.Event.RedstoneListener;
import me.Szabolcs2008.redstonebridge.Util.LastUpdateStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import me.Szabolcs2008.redstonebridge.Config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class RedstoneBridge implements ModInitializer {
    public static final String MOD_ID = "RedstoneBridge";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Bridges bridges = new Bridges();

    public static LastUpdateStorage lastUpdateStorage = new LastUpdateStorage();

    @Override
    public void onInitialize() {
        LOGGER.info("REDSTONE IRL!?");
        Config.init();
        try {
            bridges.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            BridgeCommand.register(dispatcher);
        });

        RedstoneListener.registerEvents();



    }
}
