package me.Szabolcs2008.redstonebridge.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;

import java.util.*;

public class LastUpdateStorage {
    private static HashMap<String, UpdateData> lastUpdates = new HashMap<>();


    public void setLastUpdate(String name, UpdateData data) {
        lastUpdates.put(name, data);
    }

    public void remove(String name) {
        lastUpdates.remove(name);
    }

    public UpdateData getLastUpdate(String name) {
        return lastUpdates.get(name);
    }



}
