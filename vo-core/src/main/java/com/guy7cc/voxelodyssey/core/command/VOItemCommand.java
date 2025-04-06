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

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.item.VOCoreItems;
import com.guy7cc.voxelodyssey.core.item.VOItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Represents a command descriptor for voitem commands.
 * This class implements the CommandDescriptor interface and provides functionality to handle voitem commands.
 */
public class VOItemCommand implements CommandDescriptor {
    private final CommandComposition composition = new CommandComposition()
            .add(
                    (args, sender) -> {
                        VOItem voitem = (VOItem) args.get("voitem");
                        if (sender instanceof Player player) {
                            player.getInventory().addItem(voitem.getDefaultState().getHandle());
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("l1", "give"),
                    CommandArg.registry("voitem", VOCoreItems.REGISTRY, VoxelOdysseyCore.NAMESPACE)
            );

    @Override
    public String getName() {
        return "voitem";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return composition.onCommand(commandSender, command, s, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return composition.onTabComplete(commandSender, command, s, args);
    }
}
