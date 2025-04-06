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
/**
 * This code is based on the following source. I sincerely appreciate it.
 * https://qiita.com/myui/items/6597087d6a0648ca1dec
 */
package com.guy7cc.voxelodyssey.core.common;

import java.util.function.Consumer;

/**
 * A functional interface that extends the Consumer interface to allow for throwing exceptions.
 * This interface is used to handle exceptions in a more flexible way.
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {
    @Override
    default void accept(final T e) {
        try {
            accept0(e);
        } catch (Throwable ex) {
            SneakyThrower.sneakyThrow(ex);
        }
    }

    void accept0(T e) throws Throwable;
}
