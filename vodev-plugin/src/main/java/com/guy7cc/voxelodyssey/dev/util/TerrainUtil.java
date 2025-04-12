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
package com.guy7cc.voxelodyssey.dev.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.List;
import java.util.Set;

public class TerrainUtil {
    public static boolean isAir(BlockType type){
        return type == BlockTypes.AIR || type == BlockTypes.VOID_AIR || type == BlockTypes.CAVE_AIR;
    }

    public static void replace(EditSession session, BlockVector3 pos, BlockType type, Set<BlockType> target) throws MaxChangedBlocksException {
        if (target.contains(session.getBlock(pos).getBlockType())) {
            session.setBlock(pos, type.getDefaultState());
        }
    }

    public static boolean isExposed(EditSession session, BlockVector3 pos) {
        List<BlockVector3> list = List.of(
                BlockVector3.at(1, 0, 0),
                BlockVector3.at(-1, 0, 0),
                BlockVector3.at(0, 1, 0),
                BlockVector3.at(0, -1, 0),
                BlockVector3.at(0, 0, 1),
                BlockVector3.at(0, 0, -1)
        );
        for (var offset : list) {
            if (session.getBlock(pos.add(offset)).getBlockType() == BlockTypes.AIR) return true;
        }
        return false;
    }
}
