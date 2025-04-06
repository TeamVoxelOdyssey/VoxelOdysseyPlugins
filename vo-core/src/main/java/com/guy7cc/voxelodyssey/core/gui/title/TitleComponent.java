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

import com.guy7cc.voxelodyssey.core.common.Tickable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

/**
 * Represents a component in a title.
 */
public interface TitleComponent extends Tickable {
    /**
     * Gets the paper component associated with this title component.
     *
     * @return the component
     */
    Component getPaperComponent();

    /**
     * Gets the rank of the component.
     * The rank determines the order in which components are displayed.
     *
     * @return the rank of the component
     */
    boolean shouldBeRemoved();

    static Component alignLeft(String text, TitlePosition pos) {
        Component component = Component.empty();
        int offset = 0;
        for (char c : text.toCharArray()) {
            component = component.append(Component.text(c + CharWidth.toSpaceChar(c)).color(TextColor.color(pos.r, offset, 0)));
            offset += CharWidth.toWidth(c);
        }
        return component;
    }

    static Component alignCenter(String text, TitlePosition pos) {
        Component component = Component.empty();
        int offset = 0;
        for (char c : text.toCharArray()) {
            offset += CharWidth.toWidth(c);
        }
        if (CharWidth.spaceCharExists(-offset)) {
            component = component.append(Component.text(CharWidth.toSpaceChar(-offset)));
        } else {
            StringBuilder sb = new StringBuilder();
            for (char c : text.toCharArray()) {
                sb.append(CharWidth.toSpaceChar(c));
            }
            component = component.append(Component.text(sb.toString()));
        }
        return component;
    }
}
