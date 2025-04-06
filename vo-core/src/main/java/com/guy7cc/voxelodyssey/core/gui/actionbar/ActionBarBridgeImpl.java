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
package com.guy7cc.voxelodyssey.core.gui.actionbar;

import org.bukkit.entity.Player;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of the ActionBarBridge interface.
 * This class manages the action bar components for a player.
 */
public class ActionBarBridgeImpl implements ActionBarBridge {
    private final Player player;
    private final SortedSet<ActionBarComponent> components = new TreeSet<>();
    private boolean visible;

    public ActionBarBridgeImpl(Player player, boolean visible){
        this.player = player;
        this.visible = visible;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public SortedSet<ActionBarComponent> getComponents() {
        return components;
    }

    @Override
    public void addComponent(ActionBarComponent component) {
        components.add(component);
    }

    @Override
    public void removeComponent(ActionBarComponent component) {
        components.remove(component);
    }

    @Override
    public boolean getVisible() {
        return this.visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
