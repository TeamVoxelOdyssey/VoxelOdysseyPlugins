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

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.common.Copyable;

import java.util.function.Function;
import java.util.logging.Level;

/**
 * ImmutableValueHolder is a generic class that holds a value of type T.
 * It provides a method to create an instance of the class and a method to
 * pass a function to modify the value.
 *
 * @param <T> the type of the value held by this class
 */
public class ImmutableValueHolder<T> implements Copyable<ImmutableValueHolder<T>> {
    public T value;

    private ImmutableValueHolder(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImmutableValueHolder<T> clone() {
        try {
            return (ImmutableValueHolder<T>) super.clone();
        } catch (CloneNotSupportedException exception) {
            VoxelOdysseyCore.getLogger().log(
                    Level.SEVERE,
                    "Failed to clone ImmutableValueHolder",
                    exception
            );
        }
        return null;
    }

    /**
     * Creates an instance of ImmutableValueHolder with the specified value.
     *
     * @param value the value to be held
     * @param <U>   the type of the value
     * @return an instance of ImmutableValueHolder holding the specified value
     */
    public static <U> ImmutableValueHolder<U> of(U value) {
        return new ImmutableValueHolder<>(value);
    }

    /**
     * Applies the specified function to the value held by this instance.
     *
     * @param func the function to be applied
     */
    public void pass(Function<T, T> func) {
        value = func.apply(value);
    }
}
