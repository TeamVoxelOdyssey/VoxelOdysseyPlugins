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
package com.guy7cc.voxelodyssey.dev.tool.impl;

import com.guy7cc.voxelodyssey.dev.tool.Tool;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.guy7cc.voxelodyssey.dev.tool.weight.HeightMap;
import com.guy7cc.voxelodyssey.dev.tool.weight.Kernel;
import com.guy7cc.voxelodyssey.dev.tool.weight.KernelImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.Map;

/**
 * A tool that smooths the terrain around a specified location.
 * <p>
 * This tool uses WorldEdit to smooth the terrain around the specified location.
 * </p>
 */
public class Smooth extends Tool {
    private int size;
    private Kernel<Float> kernel;

    public Smooth(int size, Kernel<Float> kernel) {
        super("smooth");
        this.size = size;
        this.kernel = kernel;
    }

    @Override
    public void apply(Player player, Location loc) {
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(actor);
        BlockVector3 targetPos = new BlockVector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Vector pDir = player.getLocation().getDirection();
        Map<Vector, PosGetter> bDir = Map.of(
                new Vector(0, -1, 0), (i, j, h) -> targetPos.add(i, h, j),
                new Vector(1, 0, 0), (i, j, h) -> targetPos.add(-h, i, j),
                new Vector(0, 0, 1), (i, j, h) -> targetPos.add(i, j, -h),
                new Vector(-1, 0, 0), (i, j, h) -> targetPos.add(h, i, j),
                new Vector(0, 0, -1), (i, j, h) -> targetPos.add(i, j, h),
                new Vector(0, 1, 0), (i, j, h) -> targetPos.add(i, -h, j)
        );
        var nearestEntry = bDir.entrySet().stream().max(Comparator.comparing(entry -> pDir.dot(entry.getKey()))).get();
        Vector nDir = nearestEntry.getKey();
        PosGetter posGetter = nearestEntry.getValue();
        try (EditSession session = localSession.createEditSession(actor)) {
            try {
                HeightMap original = new HeightMap(size);
                HeightMap smoothed = new HeightMap(size);
                KernelImpl<BlockState> topBlock = new KernelImpl<>(size, BlockTypes.AIR.getDefaultState());

                for (int i = -size; i <= size; i++) {
                    for (int j = -size; j <= size; j++) {
                        int h = 4;
                        for (; h > -4; h--) {
                            BlockVector3 top = posGetter.get(i, j, h);
                            if (session.getBlock(top).getBlockType() != BlockTypes.AIR) {
                                original.set(i, j, (float) h);
                                topBlock.set(i, j, session.getBlock(top));
                                break;
                            }
                        }
                        if (h == -4) {
                            BlockVector3 top = posGetter.get(i, j, h);
                            original.set(i, j, (float) h);
                            topBlock.set(i, j, session.getBlock(top));
                        }
                    }
                }

                float[][] w = createWeight(size, 1);
                for (int i = -size; i <= size; i++) {
                    for (int j = -size; j <= size; j++) {
                        for (int i2 = -size; i2 <= size; i2++) {
                            for (int j2 = -size; j2 <= size; j2++) {
                                smoothed.add(i, j, kernel.get(i2, j2) * original.get(i + i2, j + j2));
                            }
                        }
                        int h = Math.round(smoothed.get(i, j));
                        for (int h2 = 4; h2 > h; h2--) {
                            BlockVector3 top = posGetter.get(i, j, h2);
                            session.setBlock(top, BlockTypes.AIR.getDefaultState());
                        }
                        BlockVector3 top = posGetter.get(i, j, h);
                        session.setBlock(top, topBlock.get(i, j));
                    }
                }

            } catch (MaxChangedBlocksException exception) {
                // do nothing cuz of boring implementation
            }

            localSession.remember(session);
            session.commit();
        }
    }

    private float[][] createWeight(int size, float sigma) {
        int edge = size * 2 + 1;
        float[][] w = new float[edge][];

        double c = 2 * sigma * sigma;
        float sum = 0;
        for (int i = -size; i <= size; i++) {
            w[i + size] = new float[edge];
            for (int j = -size; j <= size; j++) {
                float value = (float) (Math.exp(-(i * i + j * j) / c));
                w[i + size][j + size] = value;
                sum += value;
            }
        }

        for (int i = -size; i <= size; i++) {
            for (int j = -size; j <= size; j++) {
                w[i + size][j + size] = w[i + size][j + size] / sum;
            }
        }
        return w;
    }

    @FunctionalInterface
    private interface PosGetter {
        BlockVector3 get(int i, int j, int h);
    }
}
