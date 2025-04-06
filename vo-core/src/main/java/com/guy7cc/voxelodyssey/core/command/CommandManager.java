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

import com.guy7cc.voxelodyssey.core.common.ExecutionOrder;
import com.guy7cc.voxelodyssey.core.common.PluginLifecycleListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * CommandManager is responsible for registering command handlers and setting up command tab completion.
 * It implements PluginLifecycleListener to handle plugin lifecycle events.
 */
public class CommandManager implements PluginLifecycleListener {
    private final JavaPlugin plugin;
    private final CommandDescriptor[] handlers;

    public CommandManager(JavaPlugin plugin, CommandDescriptor... handlers){
        this.plugin = plugin;
        this.handlers = handlers;
    }

    @Override
    @ExecutionOrder(ExecutionOrder.Order.MIDDLE)
    public void onPluginEnabled(JavaPlugin plugin) {
        for(CommandDescriptor handler : handlers) {
            setupCommand(handler.getName(), handler, plugin);
        }
    }

    private static <T extends CommandDescriptor> void setupCommand(String name, T executor, JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) return;
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
