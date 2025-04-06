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
package com.guy7cc.voxelodyssey.core.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A container for wrappers that associates a handle with its corresponding wrapper.
 *
 * @param <T> the type of the handle
 * @param <W> the type of the wrapper
 */
public abstract class WrapperContainer<T, W extends Wrapper<? extends T>> {
    private final Map<Object, W> wrapperMap;

    public WrapperContainer(int initialCapacity){
        wrapperMap = new HashMap<>(initialCapacity);
    }

    /**
     * Creates a key for the container based on the handle.
     * This method is used to determine how the handle is stored in the container.
     *
     * @param handle the handle to create a key for
     * @return the key for the container
     */
    protected abstract Object createContainerKey(T handle);

    /**
     * Retrieves the wrapper associated with the given handle.
     *
     * @param handle the handle to look up
     * @return the wrapper associated with the handle, or null if not found
     */
    public W get(T handle){
        return wrapperMap.get(createContainerKey(handle));
    }

    /**
     * Retrieves all wrappers in the container.
     *
     * @return a collection of all wrappers in the container
     */
    public Collection<W> values(){
        return wrapperMap.values();
    }

    /**
     * Puts a wrapper into the container, associating it with the given handle.
     *
     * @param handle  the handle to associate with the wrapper
     * @param wrapper the wrapper to put into the container
     * @return the previous wrapper associated with the handle, or null if there was none
     */
    protected W put(T handle, W wrapper){
        return wrapperMap.put(createContainerKey(handle), wrapper);
    }

    /**
     * Removes the wrapper associated with the given handle from the container.
     *
     * @param handle the handle to remove
     * @return the removed wrapper, or null if there was none
     */
    protected W remove(T handle){
        return wrapperMap.remove(createContainerKey(handle));
    }
}
