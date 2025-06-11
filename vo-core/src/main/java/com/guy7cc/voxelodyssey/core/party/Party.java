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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Party implements JsonSerializable<Party> {
    private UUID partyId;
    private String name;
    private boolean isPrivate;
    private UUID owner;
    private Set<UUID> members = new HashSet<>();
    private Set<UUID> inviteRequested = new HashSet<>();
    private Set<UUID> invited = new HashSet<>();
    private Set<UUID> banned = new HashSet<>();

    public Party() {
        this("Default Party", false);
    }

    public Party(String name, boolean isPrivate) {
        this.partyId = UUID.randomUUID();
        this.name = name;
        this.isPrivate = isPrivate;
    }

    public Party(String name, Player owner, boolean isPrivate){
        this.partyId = UUID.randomUUID();
        this.name = name;
        this.isPrivate = isPrivate;
        this.owner = owner.getUniqueId();
        this.members.add(owner.getUniqueId());
    }

    public String getName(){
        return name;
    }

    public UUID getId(){
        return partyId;
    }

    public boolean isPrivate(){
        return isPrivate;
    }

    public UUID getOwner(){
        return owner;
    }

    public boolean containsMember(UUID playerId){
        return members.contains(playerId);
    }

    public PartyOperationResult requestInvite(UUID playerId){
        if(members.contains(playerId)){
            return PartyOperationResult.ALREADY_IN_PARTY;
        } else if(banned.contains(playerId)){
            return PartyOperationResult.BANNED;
        } else if(inviteRequested.contains(playerId)){
            return PartyOperationResult.ALREADY_INVITE_REQUESTED;
        } else if(invited.contains(playerId)){
            return PartyOperationResult.ALREADY_INVITED;
        } else {
            inviteRequested.add(playerId);
            return PartyOperationResult.SUCCESS;
        }
    }

    public PartyOperationResult invite(UUID playerId){
        if(members.contains(playerId)){
            return PartyOperationResult.ALREADY_IN_PARTY;
        } else if(banned.contains(playerId)){
            return PartyOperationResult.BANNED;
        } else if(invited.contains(playerId)){
            return PartyOperationResult.ALREADY_INVITED;
        } else {
            invited.add(playerId);
            inviteRequested.remove(playerId);
            return PartyOperationResult.SUCCESS;
        }
    }

    public PartyOperationResult join(UUID playerId){
        if(members.contains(playerId)){
            return PartyOperationResult.ALREADY_IN_PARTY;
        } else if(banned.contains(playerId)){
            return PartyOperationResult.BANNED;
        } else if(invited.contains(playerId) || !isPrivate){
            invited.remove(playerId);
            inviteRequested.remove(playerId);
            members.add(playerId);
            return PartyOperationResult.SUCCESS;
        } else {
            return PartyOperationResult.IS_PRIVATE;
        }
    }

    public PartyOperationResult leave(UUID playerId){
        if(members.contains(playerId)){
            members.remove(playerId);
            return PartyOperationResult.SUCCESS;
        } else {
            return PartyOperationResult.NOT_IN_PARTY;
        }
    }

    public PartyOperationResult kick(UUID playerId){
        if(members.contains(playerId)){
            members.remove(playerId);
            inviteRequested.remove(playerId);
            invited.remove(playerId);
            return PartyOperationResult.SUCCESS;
        } else {
            return PartyOperationResult.NOT_IN_PARTY;
        }
    }

    public PartyOperationResult ban(UUID playerId){
        if(banned.contains(playerId)){
            return PartyOperationResult.ALREADY_BANNED;
        } else {
            members.remove(playerId);
            inviteRequested.remove(playerId);
            invited.remove(playerId);
            banned.add(playerId);
            return PartyOperationResult.SUCCESS;
        }
    }

    public PartyOperationResult unban(UUID playerId){
        if(banned.contains(playerId)){
            banned.remove(playerId);
            return PartyOperationResult.SUCCESS;
        } else {
            return PartyOperationResult.NOT_BANNED;
        }
    }

    @Override
    public Party initialize() {
        this.name = "Default Party";
        this.partyId = UUID.randomUUID();
        this.isPrivate = false;
        this.members = new HashSet<>();
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject root = new JsonObject();
        root.addProperty("id", partyId.toString());
        root.addProperty("private", isPrivate);
        root.addProperty("name", name);
        root.addProperty("owner", owner.toString());
        root.add("members", toJsonArray(members));
        root.add("inviteRequested", toJsonArray(inviteRequested));
        root.add("invited", toJsonArray(invited));
        root.add("banned", toJsonArray(banned));
        return root;
    }

    private JsonArray toJsonArray(Set<UUID> set) {
        JsonArray jsonArray = new JsonArray();
        for (UUID uuid : set) {
            jsonArray.add(uuid.toString());
        }
        return jsonArray;
    }

    @Override
    public Party fromJson(JsonElement j) throws DataFormatException {
        try{
            JsonObject o = j.getAsJsonObject();
            partyId = UUID.fromString(o.get("id").getAsString());
            isPrivate = o.get("private").getAsBoolean();
            name = o.get("name").getAsString();
            owner = UUID.fromString(o.get("owner").getAsString());
            members = fromJsonArray(o.getAsJsonArray("members"));
            inviteRequested = fromJsonArray(o.getAsJsonArray("inviteRequested"));
            invited = fromJsonArray(o.getAsJsonArray("invited"));
            banned = fromJsonArray(o.getAsJsonArray("banned"));
            return this;
        } catch (Exception e){
            throw new DataFormatException(getClass(), e);
        }
    }

    private Set<UUID> fromJsonArray(JsonArray jsonArray) {
        Set<UUID> set = new HashSet<>();
        for (JsonElement element : jsonArray) {
            set.add(UUID.fromString(element.getAsString()));
        }
        return set;
    }

    @Override
    public int hashCode() {
        return partyId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Party party = (Party) obj;
        return Objects.equals(partyId, party.partyId);
    }
}
