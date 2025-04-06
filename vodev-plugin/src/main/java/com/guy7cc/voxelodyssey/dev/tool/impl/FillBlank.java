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
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * A tool that fills blank spaces with stone blocks.
 * <p>
 * This tool uses WorldEdit to fill blank spaces with stone blocks.
 * </p>
 */
public class FillBlank extends Tool {
    private int size;

    public FillBlank(int size) {
        super("fill_blank");
        this.size = size;
    }

    @Override
    public void apply(Player player, Location loc) {
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(actor);
        BlockVector3 targetPos = new BlockVector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        try (EditSession session = localSession.createEditSession(actor)) {
            try {
                int r = 8;
                Set<BlockVector3> isNotBlank = new HashSet<>();
                for (int x = -r; x <= r; x++) {
                    for(int y = -r; y <= r; y++){
                        for (int z = -r; z <= r; z++) {
                            if (x * x + y * y + z * z > r * r) continue;
                            BlockVector3 root = targetPos.add(x, y, z);
                            if(isNotBlank.contains(root)) continue;
                            if(session.getBlock(root).getBlockType() != BlockTypes.AIR){
                                isNotBlank.add(root);
                                continue;
                            }

                            Set<BlockVector3> checked = new HashSet<>();
                            explore(session, root, checked, size);
                            if(checked.size() > size) {
                                isNotBlank.addAll(checked);
                            } else {
                                for(BlockVector3 pos : checked){
                                    session.setBlock(pos, BlockTypes.STONE.getDefaultState());
                                }
                                isNotBlank.addAll(checked);
                            }
                        }
                    }
                }
            } catch (MaxChangedBlocksException exception) {
                // do nothing cuz of boring implementation
            }

            localSession.remember(session);
            session.commit();
        }
    }

    private void explore(EditSession session, BlockVector3 pos, Set<BlockVector3> checked, int maxSize){
       if(checked.size() > maxSize) return;
       checked.add(pos);
       BlockVector3[] neighbors = new BlockVector3[]{
               pos.add(1, 0, 0),
               pos.add(-1, 0, 0),
               pos.add(0, 1, 0),
               pos.add(0, -1, 0),
               pos.add(0, 0, 1),
               pos.add(0, 0, -1)
       };
       for(BlockVector3 neighbor : neighbors){
           if(checked.contains(neighbor)) continue;
           if(session.getBlock(neighbor).getBlockType() == BlockTypes.AIR){
               explore(session, neighbor, checked, maxSize);
           }
       }
    }
}
