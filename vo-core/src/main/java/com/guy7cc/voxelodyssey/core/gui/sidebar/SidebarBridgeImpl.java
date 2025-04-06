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
package com.guy7cc.voxelodyssey.core.gui.sidebar;

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the SidebarBridge interface for managing a sidebar in Minecraft.
 * This class handles the creation, updating, and removal of sidebar components for a player.
 */
public class SidebarBridgeImpl implements SidebarBridge {
    private final Player player;
    private Component display;
    private boolean visible;
    private Scoreboard s;
    private Objective o;
    private final Map<SidebarComponent, TeamIndexPair> map = new HashMap<>();

    public SidebarBridgeImpl(Player player, Component display, boolean visible) {
        this.player = player;
        this.display = display;
        this.visible = visible;
        if (VoxelOdysseyCore.scoreboardInitialized()) {
            setScoreboard();
        }
    }

    private void setScoreboard() {
        if (s != null) return;
        s = VoxelOdysseyCore.getScoreboard(player);
        String objectiveName = VoxelOdysseyCore.NAMESPACE + "_sidebar_" + player.getName();
        o = s.getObjective(objectiveName);
        if (o != null) o.unregister();
        o = s.registerNewObjective(objectiveName, Criteria.DUMMY, display);
        o.setDisplaySlot(visible ? DisplaySlot.SIDEBAR : null);
        for (var pair : map.values()) {
            pair.team = getNewTeam();
        }
        refreshAll();
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Collection<SidebarComponent> getComponents() {
        return map.keySet();
    }

    @Override
    public void addComponent(SidebarComponent component) {
        setComponent(component, map.size());
    }

    @Override
    public void setComponent(SidebarComponent component, int index) {
        for (var pair : map.values()) {
            if (pair.index >= index) {
                pair.index++;
            }
        }
        map.put(component, new TeamIndexPair(
                o != null ? getNewTeam() : null,
                index
        ));
        component.addContainer(this);
        refreshComponent(component);
    }

    @Override
    public void removeComponent(SidebarComponent component) {
        if (!map.containsKey(component)) return;
        var removedPair = map.remove(component);
        for (var pair : map.values()) {
            if (pair.index > removedPair.index) {
                pair.index--;
            }
        }
        if (removedPair.team != null) {
            for (var entry : removedPair.team.getEntries()) {
                s.resetScores(entry);
                removedPair.team.removeEntry(entry);
            }
            removedPair.team.unregister();
        }
        component.removeContainer(this);
        refreshAll();
    }

    @Override
    public void refreshComponent(SidebarComponent component) {
        if (s == null || !map.containsKey(component)) return;
        var pair = map.get(component);
        for (var entry : pair.team.getEntries()) {
            pair.team.removeEntry(entry);
            s.resetScores(entry);
        }
        var entry = "\u00A7" + (char) ('a' + pair.index) + "\u00A7r" + component.getText();
        pair.team.addEntry(entry);
        o.getScore(entry).setScore(component.getScore());
    }

    @Override
    public void setDisplay(Component display) {
        this.display = display;
        if (o != null) {
            o.displayName(display);
        }
    }

    @Override
    public void setVisible(Boolean visible) {
        this.visible = visible;
        if (o != null) {
            o.setDisplaySlot(visible ? DisplaySlot.SIDEBAR : null);
        }
    }

    @Override
    public void dispose() {
        if (s == null) return;
        for (var pair : map.values()) {
            for (var entry : pair.team.getEntries()) {
                s.resetScores(entry);
                pair.team.removeEntry(entry);
            }
            pair.team.unregister();
        }
        s.clearSlot(DisplaySlot.SIDEBAR);
        o.unregister();

        s = null;
        o = null;
        map.clear();
    }

    private Team getNewTeam() {
        return s.registerNewTeam("Team_" + UUID.randomUUID());
    }

    private static class TeamIndexPair {
        public Team team;
        public int index;

        public TeamIndexPair(Team team, int index) {
            this.team = team;
            this.index = index;
        }
    }
}
