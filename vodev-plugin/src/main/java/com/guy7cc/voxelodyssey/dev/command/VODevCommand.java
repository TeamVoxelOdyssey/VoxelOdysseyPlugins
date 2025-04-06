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
package com.guy7cc.voxelodyssey.dev.command;

import com.guy7cc.voxelodyssey.core.command.CommandDescriptor;
import com.guy7cc.voxelodyssey.dev.VoxelOdysseyDeveloperTools;
import com.guy7cc.voxelodyssey.dev.landmark.Landmark;
import com.guy7cc.voxelodyssey.dev.landmark.LandmarkManager;
import com.guy7cc.voxelodyssey.dev.tool.impl.*;
import com.sk89q.worldedit.world.block.BlockType;
import com.guy7cc.voxelodyssey.core.command.CommandArg;
import com.guy7cc.voxelodyssey.core.command.CommandComposition;
import com.guy7cc.voxelodyssey.dev.tool.*;
import com.guy7cc.voxelodyssey.dev.tool.weight.GaussianDistribution;
import com.guy7cc.voxelodyssey.dev.landmark.Landmarks;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * A command that provides various developer tools and utilities.
 * <p>
 * This command is used to manage landmarks, set terrain organizers, and perform other developer-related tasks.
 * </p>
 */
public class VODevCommand implements CommandDescriptor {
    private final CommandComposition composition = new CommandComposition()
            .add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            String landmark = (String) args.get("landmark");
                            Location loc = Landmarks.get(landmark);
                            player.teleport(loc);
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tp")),
                    CommandArg.literal("landmark", () -> VoxelOdysseyDeveloperTools.getLandmarkManager().keySet())
            ).add(
                    (args, sender) -> {
                        LandmarkManager lm = VoxelOdysseyDeveloperTools.getLandmarkManager();
                        lm.add(
                                (String) args.get("name"),
                                new Landmark(
                                        (String) args.get("world"),
                                        (double) args.get("x"),
                                        (double) args.get("y"),
                                        (double) args.get("z"),
                                        (float) args.get("yaw"),
                                        (float) args.get("pitch")
                                )
                        );
                        return true;
                    },
                    CommandArg.literal("base", "landmark"),
                    CommandArg.literal("op", "add"),
                    CommandArg.literal("name"),
                    CommandArg.literal("world"),
                    CommandArg.rangedDouble("x", -1E6, 1E6),
                    CommandArg.rangedDouble("y", -1E6, 1E6),
                    CommandArg.rangedDouble("z", -1E6, 1E6),
                    CommandArg.rangedFloat("yaw", -1E6F, 1E6F),
                    CommandArg.rangedFloat("pitch", -1E6F, 1E6F)
            ).add(
                    (args, sender) -> {
                        LandmarkManager lm = VoxelOdysseyDeveloperTools.getLandmarkManager();
                        lm.remove((String) args.get("name"));
                        return true;
                    },
                    CommandArg.literal("base", "landmark"),
                    CommandArg.literal("op", "remove"),
                    CommandArg.literal("name", () -> VoxelOdysseyDeveloperTools.getLandmarkManager().keySet())
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            double sigma = (double) args.get("sigma");
                            int size = (int) args.get("size");
                            String kernelName = (String) args.get("kernel");
                            setTerrainOrganizer(player, new Smooth(size, new GaussianDistribution(size, (float) sigma)));
                            player.sendMessage(String.format("ガウス分布スムージングツールを設定しました。（標準偏差%s）", sigma));
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tool")),
                    CommandArg.literal("toolName", Set.of("smooth")),
                    CommandArg.literal("kernel", Set.of("gauss")),
                    CommandArg.rangedDouble("sigma", 0.1d, 10d),
                    CommandArg.rangedInt("size", 1, 16)
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            int size = (int) args.get("size");
                            int bottom = (int) args.get("bottom");
                            setTerrainOrganizer(player, new ExtendBottom(size, bottom));
                            player.sendMessage(String.format("底延長ツールを設定しました。（y座標%sまで）", bottom));
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tool")),
                    CommandArg.literal("toolName", Set.of("extend_bottom")),
                    CommandArg.rangedInt("size", 1, 16),
                    CommandArg.rangedInt("bottom", -64, 256)
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            int size = (int) args.get("size");
                            String name = (String) args.get("preset");
                            List<BlockType> preset = TerrainPresets.get(name);
                            setTerrainOrganizer(player, new Terrain(size, preset));
                            player.sendMessage(String.format("地形生成ツールを設定しました。（%sプリセット）", name));
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tool")),
                    CommandArg.literal("toolName", Set.of("terrain")),
                    CommandArg.literal("preset", TerrainPresets.names()),
                    CommandArg.rangedInt("size", 1, 16)
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            int size = (int) args.get("size");
                            String name = (String) args.get("preset");
                            List<BlockType> preset = TerrainPresets.get(name);
                            setTerrainOrganizer(player, new TerrainNoDestruction(size, preset));
                            player.sendMessage(String.format("地形生成ツール（非破壊）を設定しました。（%sプリセット）", name));
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tool")),
                    CommandArg.literal("toolName", Set.of("terrain_ndt")),
                    CommandArg.literal("preset", TerrainPresets.names()),
                    CommandArg.rangedInt("size", 1, 16)
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            int size = (int) args.get("size");
                            setTerrainOrganizer(player, new DesertMountain(size));
                            player.sendMessage("砂丘生成ツールを設定しました。");
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", Set.of("tool")),
                    CommandArg.literal("toolName", Set.of("desert_mountain")),
                    CommandArg.rangedInt("size", 1, 16)
            ).add(
                    (args, sender) -> {
                        if (sender instanceof Player player) {
                            int size = (int) args.get("size");
                            setTerrainOrganizer(player, new FillBlank(size));
                            player.sendMessage("隙間埋めツールを設定しました。");
                            return true;
                        }
                        return false;
                    },
                    CommandArg.literal("base", "tool"),
                    CommandArg.literal("toolName", "fill_blank"),
                    CommandArg.rangedInt("size", 1, 256)
            );

    @Override
    public String getName() {
        return "vodev";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return composition.onCommand(commandSender, command, s, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        return composition.onTabComplete(commandSender, command, s, args);
    }

    private void setTerrainOrganizer(Player player, Tool organizer) {
        VoxelOdysseyDeveloperTools.getToolManager().set(player, player.getInventory().getItemInMainHand().getType(), organizer);
    }
}
