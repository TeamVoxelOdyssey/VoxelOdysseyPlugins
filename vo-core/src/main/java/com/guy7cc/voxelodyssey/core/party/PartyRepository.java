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
package com.guy7cc.voxelodyssey.core.party;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.data.DataHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class PartyRepository implements DataHolder {
    private final Set<Party> parties = new HashSet<>();

    public Set<Party> getParties() {
        return Collections.unmodifiableSet(parties);
    }

    public Party fromName(String name){
        for(Party party : parties){
            if(party.getName().equalsIgnoreCase(name)){
                return party;
            }
        }
        return null;
    }

    public Party fromId(UUID id){
        for(Party party : parties){
            if(party.getId().equals(id)){
                return party;
            }
        }
        return null;
    }

    public Party fromPlayer(Player player){
        for(Party party : parties){
            if(party.containsMember(player.getUniqueId())){
                return party;
            }
        }
        return null;
    }

    public Party fromOwner(UUID uuid){
        for(Party party : parties){
            if(party.getOwner().equals(uuid)){
                return party;
            }
        }
        return null;
    }

    public Party create(Player player, String name, boolean isPrivate){
        Party party = new Party(name, player, isPrivate);
        parties.add(party);
        return party;
    }

    @Override
    public String getHolderName() {
        return "partyRepository";
    }

    @Override
    public void load(JsonObject data) {
        if(data.has("parties") && data.isJsonArray()){
            for(JsonElement element : data.getAsJsonArray("parties")){
                try{
                    Party party = new Party();
                    party.fromJson(element);
                    parties.add(party);
                } catch (Exception e){
                    VoxelOdysseyCore.getLogger().log(
                            Level.SEVERE,
                            "Failed to load party",
                            e
                    );
                }
            }
        }
    }

    @Override
    public JsonObject getWrittenData() {
        JsonObject j = new JsonObject();
        JsonObject partiesData = new JsonObject();
        for(Party party : parties){
            partiesData.add(party.getId().toString(), party.toJson());
        }
        j.add("parties", partiesData);
        return j;
    }
}
