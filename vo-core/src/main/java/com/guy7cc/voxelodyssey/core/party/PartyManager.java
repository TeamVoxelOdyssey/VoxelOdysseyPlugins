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

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PartyManager implements Listener {
    private final Map<UUID, UUID> parents = new TreeMap<>();
    private final Map<UUID, Set<UUID>> children = new TreeMap<>();
    private final Map<UUID, Set<UUID>> requests = new TreeMap<>();
    private final Map<UUID, Set<UUID>> invites = new TreeMap<>();

    public PartyManager(){

    }

    private UUID getParent(UUID childId) {
        return parents.getOrDefault(childId, childId);
    }

    private Set<UUID> getChildren(UUID parentId) {
        return children.getOrDefault(parentId, Collections.emptySet());
    }

    private Set<UUID> getRequests(UUID playerId) {
        return requests.getOrDefault(playerId, Collections.emptySet());
    }

    private Set<UUID> getInvites(UUID playerId) {
        return invites.getOrDefault(playerId, Collections.emptySet());
    }

    public List<Player> getParty(Player player) {
        UUID playerId = player.getUniqueId();
        List<Player> members = new ArrayList<>();

        UUID parentId = getParent(playerId);
        members.add(Bukkit.getPlayer(parentId));
        for (UUID childId : getChildren(parentId)) {
            Player child = Bukkit.getPlayer(childId);
            members.add(child);
        }

        return members;
    }

    public List<List<Player>> getAllParties() {
        List<List<Player>> parties = new ArrayList<>();
        Set<UUID> processed = new HashSet<>();

        for (UUID playerId : Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).toList()) {
            if (processed.contains(playerId)) continue;

            List<Player> party = getParty(Bukkit.getPlayer(playerId));
            for (Player member : party) {
                processed.add(member.getUniqueId());
            }

            parties.add(party);
        }

        return parties;
    }

    public PartyOperationResult request(Player player, Player target){
        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();

        if (playerId.equals(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.cannot_request_self").color(TextColor.color(0xFF5555)));
            return PartyOperationResult.CANNOT_REQUEST_SELF;
        }
        if (getRequests(playerId).contains(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.request_already_sent", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_REQUESTED;
        }
        if (getInvites(targetId).contains(playerId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.invite_already_received", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_INVITED;
        }
        if (getParent(playerId).equals(getParent(targetId))) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.already_in_party", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_IN_PARTY;
        }

        requests.computeIfAbsent(playerId, k -> new TreeSet<>()).add(targetId);
        player.sendMessage(Component.translatable("commands.voxelodyssey.party.request_sent", Component.text(target.getName()).color(TextColor.color(0x55FF55))));
        target.sendMessage(Component.translatable("commands.voxelodyssey.party.request_received", Component.text(player.getName()).color(TextColor.color(0x55FF55)))
                .appendSpace()
                .append(Component.translatable("commands.voxelodyssey.party.invite_button").color(TextColor.color(0x55FF55)).clickEvent(ClickEvent.runCommand("/party invite " + player.getName())))
        );
        return PartyOperationResult.SUCCESS;
    }

    public PartyOperationResult invite(Player player, Player target) {
        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();

        if (playerId.equals(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.cannot_invite_self").color(TextColor.color(0xFF5555)));
            return PartyOperationResult.CANNOT_INVITE_SELF;
        }
        if (getInvites(playerId).contains(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.invite_already_sent", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_INVITED;
        }
        if (getParent(playerId).equals(getParent(targetId))) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.already_in_party", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_IN_PARTY;
        }

        Set<UUID> requestsSet = getRequests(targetId);
        if(requestsSet.contains(playerId)) {
            requestsSet.remove(playerId);
            if (requestsSet.isEmpty()) {
                requests.remove(targetId);
            }
        }

        invites.computeIfAbsent(playerId, k -> new TreeSet<>()).add(targetId);
        player.sendMessage(Component.translatable("commands.voxelodyssey.party.invite_sent", Component.text(target.getName()).color(TextColor.color(0x55FF55))));
        target.sendMessage(Component.translatable("commands.voxelodyssey.party.invite_received", Component.text(player.getName()).color(TextColor.color(0x55FF55)))
                .appendSpace()
                .append(Component.translatable("commands.voxelodyssey.party.join_button").color(TextColor.color(0x55FF55)).clickEvent(ClickEvent.runCommand("/party join " + player.getName())))
        );
        return PartyOperationResult.SUCCESS;
    }

    public PartyOperationResult join(Player player, Player target){
        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();

        if(playerId.equals(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.cannot_join_self").color(TextColor.color(0xFF5555)));
            return PartyOperationResult.CANNOT_JOIN_SELF;
        }
        if (getParent(playerId).equals(getParent(targetId))) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.already_in_party", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.ALREADY_IN_PARTY;
        }
        if (!getInvites(targetId).contains(playerId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.not_invited", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.NOT_INVITED;
        }

        parents.put(playerId, targetId);
        children.computeIfAbsent(targetId, k -> new TreeSet<>()).add(playerId);
        invites.get(targetId).remove(playerId);
        if(invites.get(targetId).isEmpty()) invites.remove(targetId);

        player.sendMessage(Component.translatable("commands.voxelodyssey.party.joined", Component.text(target.getName()).color(TextColor.color(0x55FF55))));
        target.sendMessage(Component.translatable("commands.voxelodyssey.party.player_joined", Component.text(player.getName()).color(TextColor.color(0x55FF55))));

        return PartyOperationResult.SUCCESS;
    }

    public PartyOperationResult leave(Player player) {
        UUID playerId = player.getUniqueId();

        if(getParent(playerId).equals(playerId) && getChildren(playerId).isEmpty()) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.not_in_party").color(TextColor.color(0xFF5555)));
            return PartyOperationResult.NOT_IN_PARTY;
        }

        // Notify its parent
        parents.remove(playerId);
        for (UUID parentId : children.keySet()) {
            if(!children.containsKey(parentId)) continue;
            Set<UUID> childrenSet = children.get(parentId);
            // Remove the player from the parent's children set
            childrenSet.remove(playerId);
            // Notify the parent that a child has left
            Player parent = Bukkit.getPlayer(parentId);
            if(parent != null) {
                parent.sendMessage(Component.translatable("commands.voxelodyssey.party.player_left", Component.text(player.getName()).color(TextColor.color(0x55FF55))));
            }
            if (childrenSet.isEmpty()) {
                children.remove(parentId);
            }
        }

        // Notify its children
        Set<UUID> ownChildrenSet = getChildren(playerId);
        if(!ownChildrenSet.isEmpty()){
            UUID firstChildId = ownChildrenSet.iterator().next();
            parents.remove(firstChildId);
            for(UUID childId : ownChildrenSet) {
                // Notify the child that their parent has left
                Player child = Bukkit.getPlayer(childId);
                child.sendMessage(Component.translatable("commands.voxelodyssey.party.player_left", Component.text(player.getName()).color(TextColor.color(0x55FF55))));
                if(childId.equals(firstChildId)) continue;
                // Reassign the first child as the parent of all other children
                parents.put(childId, firstChildId);
                children.computeIfAbsent(firstChildId, k -> new TreeSet<>()).add(childId);
            }
        }
        children.remove(playerId);

        player.sendMessage(Component.translatable("commands.voxelodyssey.party.left"));
        return PartyOperationResult.SUCCESS;
    }

    public PartyOperationResult kick(Player player, Player target) {
        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();

        if( playerId.equals(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.cannot_kick_self").color(TextColor.color(0xFF5555)));
            return PartyOperationResult.CANNOT_KICK_SELF;
        }
        if (!getChildren(playerId).contains(targetId)) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.cannot_kick_non_child", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.CANNOT_KICK_NON_CHILD;
        }
        if (!playerId.equals(getParent(targetId))) {
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.non_owner_cannot_kick", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
            return PartyOperationResult.NON_OWNER_CANNOT_KICK;
        }

        children.get(playerId).remove(targetId);
        parents.remove(targetId);
        Player child = Bukkit.getPlayer(targetId);
        if (child != null) {
            child.sendMessage(Component.translatable("commands.voxelodyssey.party.was_kicked", Component.text(player.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
        }
        player.sendMessage(Component.translatable("commands.voxelodyssey.party.kicked", Component.text(target.getName()).color(TextColor.color(0x55FF55))).color(TextColor.color(0xFF5555)));
        return PartyOperationResult.SUCCESS;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Remove player from requests and invites
        requests.remove(playerId);
        invites.remove(playerId);

        leave(player);
    }
}
