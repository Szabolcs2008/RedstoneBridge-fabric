package me.Szabolcs2008.redstonebridge.Config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Bridges {

    public static final String X = "location-x";
    public static final String Y = "location-y";
    public static final String Z = "location-z";
    public static final String URL = "url";
    public static final String MODE = "mode";
    public static final String ENABLED = "enabled";


    public static final String DIGITAL = "DIGITAL";
    public static final String ANALOGUE = "ANALOGUE";
    public static final String RGB = "RGB";


    private final Path configDir = Config.getConfigDir();
    private ObjectNode bridges;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<BlockPos> validCoordinates = new ArrayList<>();

    public void init() throws IOException {

        if (!new File(configDir+"/RedstoneBridge/bridges.json").exists()) {
            try {
                ObjectNode json = objectMapper.createObjectNode();

                objectMapper.writeValue(new File(configDir+"/RedstoneBridge/bridges.json"), json);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        bridges = (ObjectNode) objectMapper.readTree(new File(configDir+"/RedstoneBridge/bridges.json"));

        for (String name : listBridges()) {
            JsonNode bridge = bridges.get(name);
            BlockPos loc = new BlockPos(bridge.get(X).asInt(), bridge.get(Y).asInt(), bridge.get(Z).asInt());

            if (!validCoordinates.contains(loc)) {
                validCoordinates.add(loc);
            }
        }

    }

    public void saveBridges() {
        File file = new File(configDir+"/RedstoneBridge/bridges.json");
        try {
            objectMapper.writeValue(file, bridges);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public JsonNode getBridge(String name) {
        return bridges.get(name);
    }

    public ObjectNode getBridgesObject() {
        return bridges;
    }

    public void addBridge(String name, String mode, BlockPos location, String url) {
        ObjectNode bridge = objectMapper.createObjectNode();

        bridge.put("mode", mode);
        bridge.put("location-x", location.getX());
        bridge.put("location-y", location.getY());
        bridge.put("location-z", location.getZ());
        bridge.put("enabled", true);
        bridge.put("url", url);

        bridges.set(name, bridge);
        validCoordinates.add(location);
    }

    public void removeBridge(String name) {
        BlockPos old_loc = new BlockPos(bridges.get(name).get(X).asInt(), bridges.get(name).get(Y).asInt(), bridges.get(name).get(Z).asInt());
        bridges.remove(name);
        validCoordinates.remove(old_loc);
    }

    public void moveBridge(String name, BlockPos newLocaction) {
        BlockPos old_loc = new BlockPos(bridges.get(name).get(X).asInt(), bridges.get(name).get(Y).asInt(), bridges.get(name).get(Z).asInt());
        validCoordinates.remove(old_loc);

        ObjectNode bridge = (ObjectNode) bridges.get(name);
        bridge.put(X, newLocaction.getX());
        bridge.put(Y, newLocaction.getY());
        bridge.put(Z, newLocaction.getZ());

        validCoordinates.add(newLocaction);
        bridges.set(name, bridge);
    }

    public void setURL(String name, String url) {
        ObjectNode bridge = (ObjectNode) bridges.get(name);
        bridge.put("url", url);

        bridges.set(name, bridge);
    }

    public boolean isValidCoordinate(BlockPos location) {
        return validCoordinates.contains(location);
    }

    public List<String> listBridges() {
        List<String> output = new ArrayList<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = RedstoneBridge.bridges.getBridgesObject().fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> item = it.next();
            output.add(item.getKey());
        }
        return output;
    }

    public void toggleBridge(String name, boolean enabled) {
        ObjectNode bridge = (ObjectNode) bridges.get(name);
        bridge.put("enabled", enabled);

        bridges.set(name, bridge);
    }

    public boolean isEnabled(String name) {
        return bridges.get(name).get(ENABLED).asBoolean();
    }

}
