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
package com.guy7cc.voxelodyssey.core.gui.title;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * CharWidth is a utility class that provides functionality to determine the width of characters
 * and manage character width groups.
 * <p>
 * It allows adding character width groups, validating them, and converting characters to their
 * corresponding width.
 */
public class CharWidth {
    public static final int DEFAULT_WIDTH = 8;

    private static final List<Group> charToWidth = new ArrayList<>();
    private static final Map<Integer, Character> widthToSpaceChar = new HashMap<>();

    static {
        addWidthUnsafe('\uE100', '\uE15A', 9);
        validateGroups();

        for (int i = 1; i <= 16; i++) {
            addSpaceChar(-i, (char) ('\uE000' + i));
        }
    }

    public static char toSpaceChar(int width) {
        return widthToSpaceChar.getOrDefault(width, '\uE000');
    }

    public static char toSpaceChar(char c) {
        return toSpaceChar(-toWidth(c));
    }

    public static void addSpaceChar(int width, char c) {
        widthToSpaceChar.put(width, c);
    }

    public static boolean spaceCharExists(int width) {
        return widthToSpaceChar.containsKey(width);
    }

    public static int toWidth(char c) {
        Group virtualGroup = new Group(c, (char) (c + 1), 0);
        int index = Collections.binarySearch(charToWidth, virtualGroup);
        if (index >= 0) {
            return charToWidth.get(index).width;
        } else {
            int insertionPoint = -index - 1;
            if (insertionPoint == 0) return DEFAULT_WIDTH;
            else {
                Group group = charToWidth.get(insertionPoint - 1);
                if (group.contains(c)) return group.width;
                else return DEFAULT_WIDTH;
            }
        }
    }

    public static void addWidth(char start, char end, int width) {
        addWidthUnsafe(start, end, width);
        validateGroups();
    }

    private static void addWidthUnsafe(char start, char end, int width) {
        charToWidth.add(new Group(start, end, width));
    }

    private static void validateGroups() {
        for (int i = 0; i < charToWidth.size() - 1; i++) {
            if (charToWidth.get(i).contains(charToWidth.get(i + 1).start)) {
                throw new IllegalStateException("Char groups are overlapping");
            }
        }
    }

    private static class Group implements Comparable<Group> {
        public final char start;
        public final char end;
        public final int width;

        public Group(char start, char end, int width) {
            if (start >= end) throw new IllegalArgumentException("start must be lower than end");
            this.start = start;
            this.end = end;
            this.width = width;
        }

        public boolean contains(char c) {
            return start <= c && c < end;
        }

        @Override
        public int compareTo(@NotNull Group o) {
            return this.start - o.start;
        }
    }
}
