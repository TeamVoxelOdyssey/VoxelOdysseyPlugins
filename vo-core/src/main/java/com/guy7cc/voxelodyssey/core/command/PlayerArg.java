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
package com.guy7cc.voxelodyssey.core.command;

import com.guy7cc.voxelodyssey.core.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerArg implements CommandArg<Player> {
    private final String name;

    public PlayerArg(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        Player player = Bukkit.getPlayer(arg);
        if (player != null) return true;
        try{
            return Bukkit.getPlayer(UUID.fromString(arg)) != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Player get(String arg) {
        Player player = Bukkit.getPlayer(arg);
        if (player != null) return player;
        try{
            return Bukkit.getPlayer(UUID.fromString(arg));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<String> onTabComplete(String arg) {
        return StringUtil.getOptions(arg, Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
    }
}
