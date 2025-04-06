/*
 * Copyright (C) 2025 TeamVoxelOdyssey
 *
 * This file is part of VoxelOdysseyPlugins.
 *
 * VoxelOdysseyPlugins is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VoxelOdysseyPlugins is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VoxelOdysseyPlugins. If not, see <https://www.gnu.org/licenses/>.
 */
package com.guy7cc.voxelodyssey.dev.landmark;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class that manages landmarks in the world.
 */
public class LandmarkManager implements DataHolder {
    private final JavaPlugin plugin;
    private final Map<String, Landmark> map = new HashMap<>();

    public LandmarkManager(JavaPlugin plugin){
        this.plugin = plugin;
        map.put("world1", new Landmark("world", 52.5, 14.0, -25.5, -120f, 0f));
        map.put("world1_puzzle", new Landmark("world", 165.5, 3.0, -417.5, 0, 0));
        map.put("world2_start", new Landmark("world", 255.5, 11.0, -1008.5, 115f, -11f));
        map.put("world2_underground", new Landmark("world", 40.5, -34.0, -1004.5, -135, 0));
        map.put("hub", new Landmark("world", 0.5, 0.0, 1000.5, 0f, 0f));
    }

    public Landmark add(String name, Location loc){
        return map.put(name, Landmark.fromLocation(loc));
    }

    public Landmark add(String name, Landmark landmark){
        return map.put(name, landmark);
    }

    public Landmark remove(String name){
        return map.remove(name);
    }

    public Set<String> keySet(){
        return map.keySet();
    }

    @Override
    public String getHolderName() {
        return "landmark";
    }

    @Override
    public void load(JsonObject jsonObject) {
        for(String key : jsonObject.keySet()){
            try{
                JsonElement element = jsonObject.get(key);
                Landmark landmark = new Landmark().fromJson(element);
                map.put(key, landmark);
            } catch(Exception e){
                plugin.getLogger().warning("Invalid landmark data found, ignoring it");
            }
        }
    }

    @Override
    public JsonObject getWrittenData() {
        JsonObject o = new JsonObject();
        for(var entry : map.entrySet()){
            o.add(entry.getKey(), entry.getValue().toJson());
        }
        return o;
    }
}
