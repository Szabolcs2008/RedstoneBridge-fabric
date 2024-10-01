package me.Szabolcs2008.redstonebridge;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedstoneBridgeClient implements ClientModInitializer {

    public static final String MOD_ID = "RedstoneBridge";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Client side too????");
    }
}
