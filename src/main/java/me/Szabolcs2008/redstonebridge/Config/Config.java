package me.Szabolcs2008.redstonebridge.Config;

import com.fasterxml.jackson.core.json.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import me.Szabolcs2008.redstonebridge.RedstoneBridge;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Config {

    private static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static JsonNode config;
    public static final ArrayList<String> whitelisted_blocks = new ArrayList<>();

    public static void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();

        json.put("debug", false);

        json.put("enable-whitelist", true);

        json.put("required-permission-level", 4);

        ObjectNode json2 = objectMapper.createObjectNode();
        json2.put("0", "#000000");
        json2.put("1", "#ff0000");
        json2.put("2", "#ff6600");
        json2.put("3", "#ffcc00");
        json2.put("4", "#cbff00");
        json2.put("5", "#65ff00");
        json2.put("6", "#00ff00");
        json2.put("7", "#00ff66");
        json2.put("8", "#00ffcb");
        json2.put("9", "#00cbff");
        json2.put("10", "#0066ff");
        json2.put("11", "#0000ff");
        json2.put("12", "#6500ff");
        json2.put("13", "#cc00ff");
        json2.put("14", "#ff00cb");
        json2.put("15", "#ff0066");
        json.set("colors", json2);

        ArrayNode blocks = objectMapper.createArrayNode();
        blocks.add("minecraft:acacia_button");
        blocks.add("minecraft:acacia_door");
        blocks.add("minecraft:acacia_fence_gate");
        blocks.add("minecraft:acacia_pressure_plate");
        blocks.add("minecraft:acacia_trapdoor");
        blocks.add("minecraft:bamboo_button");
        blocks.add("minecraft:bamboo_door");
        blocks.add("minecraft:bamboo_fence_gate");
        blocks.add("minecraft:bamboo_pressure_plate");
        blocks.add("minecraft:bamboo_trapdoor");
        blocks.add("minecraft:birch_button");
        blocks.add("minecraft:birch_door");
        blocks.add("minecraft:birch_fence_gate");
        blocks.add("minecraft:birch_pressure_plate");
        blocks.add("minecraft:birch_trapdoor");
        blocks.add("minecraft:calibrated_sculk_sensor");
        blocks.add("minecraft:cherry_button");
        blocks.add("minecraft:cherry_door");
        blocks.add("minecraft:cherry_fence_gate");
        blocks.add("minecraft:cherry_pressure_plate");
        blocks.add("minecraft:cherry_trapdoor");
        blocks.add("minecraft:comparator");
        blocks.add("minecraft:copper_bulb");
        blocks.add("minecraft:copper_door");
        blocks.add("minecraft:copper_trapdoor");
        blocks.add("minecraft:crimson_button");
        blocks.add("minecraft:crimson_door");
        blocks.add("minecraft:crimson_fence_gate");
        blocks.add("minecraft:crimson_pressure_plate");
        blocks.add("minecraft:crimson_trapdoor");
        blocks.add("minecraft:dark_oak_button");
        blocks.add("minecraft:dark_oak_door");
        blocks.add("minecraft:dark_oak_fence_gate");
        blocks.add("minecraft:dark_oak_pressure_plate");
        blocks.add("minecraft:dark_oak_trapdoor");
        blocks.add("minecraft:dispenser");
        blocks.add("minecraft:dropper");
        blocks.add("minecraft:exposed_copper_bulb");
        blocks.add("minecraft:exposed_copper_door");
        blocks.add("minecraft:exposed_copper_trapdoor");
        blocks.add("minecraft:heavy_weighted_pressure_plate");
        blocks.add("minecraft:iron_door");
        blocks.add("minecraft:iron_trapdoor");
        blocks.add("minecraft:jungle_button");
        blocks.add("minecraft:jungle_door");
        blocks.add("minecraft:jungle_fence_gate");
        blocks.add("minecraft:jungle_pressure_plate");
        blocks.add("minecraft:jungle_trapdoor");
        blocks.add("minecraft:lever");
        blocks.add("minecraft:light_weighted_pressure_plate");
        blocks.add("minecraft:mangrove_button");
        blocks.add("minecraft:mangrove_door");
        blocks.add("minecraft:mangrove_fence_gate");
        blocks.add("minecraft:mangrove_pressure_plate");
        blocks.add("minecraft:mangrove_trapdoor");
        blocks.add("minecraft:note_block");
        blocks.add("minecraft:oak_button");
        blocks.add("minecraft:oak_door");
        blocks.add("minecraft:oak_fence_gate");
        blocks.add("minecraft:oak_pressure_plate");
        blocks.add("minecraft:oak_trapdoor");
        blocks.add("minecraft:observer");
        blocks.add("minecraft:oxidized_copper_bulb");
        blocks.add("minecraft:oxidized_copper_door");
        blocks.add("minecraft:oxidized_copper_trapdoor");
        blocks.add("minecraft:piston");
        blocks.add("minecraft:polished_blackstone_button");
        blocks.add("minecraft:polished_blackstone_pressure_plate");
        blocks.add("minecraft:redstone_torch");
        blocks.add("minecraft:redstone_wall_torch");
        blocks.add("minecraft:redstone_wire");
        blocks.add("minecraft:repeater");
        blocks.add("minecraft:sculk_sensor");
        blocks.add("minecraft:spruce_button");
        blocks.add("minecraft:spruce_door");
        blocks.add("minecraft:spruce_fence_gate");
        blocks.add("minecraft:spruce_pressure_plate");
        blocks.add("minecraft:spruce_trapdoor");
        blocks.add("minecraft:sticky_piston");
        blocks.add("minecraft:stone_button");
        blocks.add("minecraft:stone_pressure_plate");
        blocks.add("minecraft:warped_button");
        blocks.add("minecraft:warped_door");
        blocks.add("minecraft:warped_fence_gate");
        blocks.add("minecraft:warped_pressure_plate");
        blocks.add("minecraft:warped_trapdoor");
        blocks.add("minecraft:waxed_copper_bulb");
        blocks.add("minecraft:waxed_copper_door");
        blocks.add("minecraft:waxed_copper_trapdoor");
        blocks.add("minecraft:waxed_exposed_copper_bulb");
        blocks.add("minecraft:waxed_exposed_copper_door");
        blocks.add("minecraft:waxed_exposed_copper_trapdoor");
        blocks.add("minecraft:waxed_oxidized_copper_bulb");
        blocks.add("minecraft:waxed_oxidized_copper_door");
        blocks.add("minecraft:waxed_oxidized_copper_trapdoor");
        blocks.add("minecraft:waxed_weathered_copper_bulb");
        blocks.add("minecraft:waxed_weathered_copper_door");
        blocks.add("minecraft:waxed_weathered_copper_trapdoor");
        blocks.add("minecraft:weathered_copper_bulb");
        blocks.add("minecraft:weathered_copper_door");
        blocks.add("minecraft:weathered_copper_trapdoor");

        json.set("whitelisted-blocks", blocks);





        new File(configDir+"/RedstoneBridge").mkdirs();
        if (!new File(configDir+"/RedstoneBridge/config.json").exists()) {
            try {
                objectMapper.writeValue(new File(configDir+"/RedstoneBridge/config.json"), json);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }

        try {
            config = objectMapper.readTree(new File(configDir+"/RedstoneBridge/config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectmapper = new ObjectMapper();

        JsonNode _blocks = config.get("whitelisted-blocks");
        String[] wl_blocks = objectmapper.convertValue(_blocks, String[].class);

        for (String block : wl_blocks) {
            whitelisted_blocks.add("Block{"+block+"}");
        }
    }

    public static JsonNode getConfig(){
        return config;
    }

    public static Path getConfigDir() {
        return configDir;
    }

    public static ArrayList<String> getWhitelistedBlocks() {
        return whitelisted_blocks;
    }
}
