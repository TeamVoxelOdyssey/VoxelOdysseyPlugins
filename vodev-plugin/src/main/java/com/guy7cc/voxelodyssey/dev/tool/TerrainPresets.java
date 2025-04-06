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
package com.guy7cc.voxelodyssey.dev.tool;

import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

import java.util.*;

/**
 * A class that holds all the terrain presets.
 * <p>
 * This class is used to register and retrieve terrain presets.
 * </p>
 */
public class TerrainPresets {
    private static final Map<String, List<BlockType>> presets = new HashMap<>();

    static {
        new Builder("world1")
                .add(BlockTypes.GRASS_BLOCK)
                .add(BlockTypes.DIRT)
                .add(BlockTypes.LIGHT_GRAY_CONCRETE)
                .add(BlockTypes.STONE, 3)
                .add(BlockTypes.GRAY_CONCRETE, 5)
                .add(BlockTypes.BLACK_CONCRETE, 5)
                .dispatch(presets);
        new Builder("world2_sand")
                .add(BlockTypes.SAND, 6)
                .add(BlockTypes.SANDSTONE, 6)
                .add(BlockTypes.WHITE_TERRACOTTA)
                .add(BlockTypes.LIGHT_GRAY_TERRACOTTA)
                .add(BlockTypes.BROWN_TERRACOTTA, 2)
                .add(BlockTypes.SOUL_SOIL, 15)
                .add(BlockTypes.GRAVEL, 3)
                .add(BlockTypes.STONE, 10)
                .add(BlockTypes.TUFF, 15)
                .add(BlockTypes.DEEPSLATE, 10)
                .add(BlockTypes.BEDROCK, 5)
                .dispatch(presets);
        new Builder("world2_sandstone")
                .add(BlockTypes.SANDSTONE, 12)
                .add(BlockTypes.WHITE_TERRACOTTA)
                .add(BlockTypes.LIGHT_GRAY_TERRACOTTA)
                .add(BlockTypes.BROWN_TERRACOTTA, 2)
                .add(BlockTypes.SOUL_SOIL, 15)
                .add(BlockTypes.GRAVEL, 3)
                .add(BlockTypes.STONE, 10)
                .add(BlockTypes.TUFF, 15)
                .add(BlockTypes.DEEPSLATE, 10)
                .add(BlockTypes.BEDROCK, 5)
                .dispatch(presets);
        new Builder("world2_red_sand")
                .add(BlockTypes.RED_SAND, 6)
                .add(BlockTypes.RED_SANDSTONE, 6)
                .add(BlockTypes.ORANGE_TERRACOTTA)
                .add(BlockTypes.RED_TERRACOTTA)
                .add(BlockTypes.BROWN_TERRACOTTA, 2)
                .add(BlockTypes.SOUL_SOIL, 15)
                .add(BlockTypes.GRAVEL, 3)
                .add(BlockTypes.STONE, 10)
                .add(BlockTypes.TUFF, 15)
                .add(BlockTypes.DEEPSLATE, 10)
                .add(BlockTypes.BEDROCK, 5)
                .dispatch(presets);
        new Builder("world2_red_sandstone")
                .add(BlockTypes.RED_SANDSTONE, 12)
                .add(BlockTypes.ORANGE_TERRACOTTA)
                .add(BlockTypes.RED_TERRACOTTA)
                .add(BlockTypes.BROWN_TERRACOTTA, 2)
                .add(BlockTypes.SOUL_SOIL, 15)
                .add(BlockTypes.GRAVEL, 3)
                .add(BlockTypes.STONE, 10)
                .add(BlockTypes.TUFF, 15)
                .add(BlockTypes.DEEPSLATE, 10)
                .add(BlockTypes.BEDROCK, 5)
                .dispatch(presets);
        new Builder("world2_bottom")
                .add(BlockTypes.GRAVEL, 3)
                .add(BlockTypes.STONE, 3)
                .add(BlockTypes.TUFF, 5)
                .add(BlockTypes.DEEPSLATE, 7)
                .add(BlockTypes.BEDROCK, 5)
                .dispatch(presets);
    }

    public static Set<String> names() {
        return presets.keySet();
    }

    public static List<BlockType> get(String name) {
        return presets.get(name);
    }

    private static class Builder {
        private final String name;
        private final List<BlockType> preset = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder add(BlockType type) {
            preset.add(type);
            return this;
        }

        public Builder add(BlockType type, int num) {
            for (int i = 0; i < num; i++) preset.add(type);
            return this;
        }

        public void dispatch(Map<String, List<BlockType>> presets) {
            presets.put(name, preset);
        }
    }
}
