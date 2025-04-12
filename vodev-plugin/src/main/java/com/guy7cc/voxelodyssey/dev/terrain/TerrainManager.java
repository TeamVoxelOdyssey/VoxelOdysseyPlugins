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
package com.guy7cc.voxelodyssey.dev.terrain;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataHolder;
import com.guy7cc.voxelodyssey.dev.VODevPlugin;
import com.guy7cc.voxelodyssey.dev.VoxelOdysseyDeveloperTools;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.entity.Player;

import java.util.*;

public class TerrainManager implements DataHolder {
    private final Map<String, List<BlockType>> presets = new HashMap<>();

    public boolean registerTerrainFromSelection(Player player, String name){
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(actor);
        try(EditSession editSession = localSession.createEditSession(actor)){
            Region region = localSession.getSelection();
            BlockVector3 min = region.getMinimumPoint();
            BlockVector3 max = region.getMaximumPoint();
            if(min.x() != max.x() || min.z() != max.z()) return false;
            BlockVector3 pos = max;
            List<BlockType> preset = new ArrayList<>();
            while(pos.y() >= min.y()){
                preset.add(editSession.getBlock(pos).getBlockType());
                pos = pos.add(0, -1, 0);
            }
            presets.put(name, preset);
            return true;
        } catch (IncompleteRegionException e) {
            return false;
        }
    }

    public List<BlockType> get(String name){
        return presets.get(name);
    }

    public List<BlockType> remove(String name){
        return presets.remove(name);
    }

    public Set<String> keySet(){
        return presets.keySet();
    }

    @Override
    public String getHolderName() {
        return "terrain";
    }

    @Override
    public void load(JsonObject data) {
        for(String key : data.keySet()){
            JsonElement e1 = data.get(key);
            if(!e1.isJsonArray()){
                VODevPlugin.getPlugin().getLogger().warning("Faid to load terrain " + key + ", ignoring it");
                continue;
            }
            JsonArray array = e1.getAsJsonArray();
            List<BlockType> preset = new ArrayList<>();
            boolean failed = false;
            for(JsonElement e2 : array){
                if(!e2.isJsonPrimitive()) {
                    failed = true;
                    break;
                }
                String id = e2.getAsString();
                BlockType type = BlockTypes.get(id);
                preset.add(type);
            }
            if(failed){
                VODevPlugin.getPlugin().getLogger().warning("Faid to load terrain " + key + ", ignoring it");
            } else {
                presets.put(key, preset);
            }
        }
    }

    @Override
    public JsonObject getWrittenData() {
        JsonObject root = new JsonObject();
        for(var entry : presets.entrySet()){
            List<BlockType> preset = entry.getValue();
            JsonArray array = new JsonArray();
            for(BlockType type : preset){
                array.add(type.id());
            }
            root.add(entry.getKey(), array);
        }
        return root;
    }
}
