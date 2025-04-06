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
package com.guy7cc.voxelodyssey.core.property;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a state that can hold properties.
 *
 * @param <T> The type of the owner of this state.
 * @param <S> The type of the state itself.
 */
public interface State<T, S extends State<T, S>> {
    T getOwner();

    boolean containsProperty(@NotNull Property<?> property);

    <U> U getProperty(@NotNull Property<U> property);

    <U> S setProperty(@NotNull Property<U> property, U value);

    default <U> S setDefaultProperty(@NotNull Property<U> property) {
        return setProperty(property, property.defaultValue());
    }
}
