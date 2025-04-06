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

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A utility class that provides methods for handling exceptions in a "sneaky" way.
 * It allows you to throw checked exceptions without declaring them in the method signature.
 */
public final class SneakyThrower {
    private SneakyThrower() {

    }

    /**
     * Wraps a ThrowingConsumer into a standard Consumer.
     * This allows you to use a ThrowingConsumer in places where a standard Consumer is expected.
     *
     * @param consumer the ThrowingConsumer to wrap
     * @return a standard Consumer that wraps the ThrowingConsumer
     * @param <T> the type of the input to the consumer
     */
    @NotNull
    public static <T> Consumer<T> sneaky(@NotNull final ThrowingConsumer<T> consumer) {
        return consumer;
    }

    /**
     * Throws a Throwable without declaring it in the method signature.
     * This is a "sneaky" way to throw checked exceptions.
     *
     * @param throwable the Throwable to throw
     * @param <E>       the type of the Throwable
     * @throws E the Throwable being thrown
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(@NotNull Throwable throwable) throws E {
        throw (E) throwable;
    }
}
