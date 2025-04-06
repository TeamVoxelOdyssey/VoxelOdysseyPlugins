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
import java.util.List;

/**
 * Represents an object that can be ticked.
 * This interface provides a method to tick the object and its children.
 */
public interface Tickable {
    /**
     * Ticks the object and its children.
     * This method can be overridden to provide custom tick behavior.
     *
     * @param globalTick the global tick count
     */
    default void tick(int globalTick){
        for(Tickable child : getTickables()){
            child.tick(globalTick);
        }
    }

    /**
     * Returns a collection of child Tickable objects.
     * This method can be overridden to provide a custom collection of child Tickables.
     *
     * @return a collection of child Tickable objects
     */
    default Collection<? extends Tickable> getTickables(){
        return List.of();
    }

    /**
     * Represents a generic tickable object that can be ticked with an argument.
     *
     * @param <T> the type of the argument
     */
    interface Generic<T> {
        /**
         * Ticks the object with the given argument.
         * This method can be overridden to provide custom tick behavior with an argument.
         *
         * @param globalTick the global tick count
         * @param arg        the argument to tick with
         */
        default void tick(int globalTick, T arg){
            for(Generic<T> child : getChildren()){
                child.tick(globalTick, arg);
            }
        }

        /**
         * Returns a collection of child Generic objects.
         * This method can be overridden to provide a custom collection of child Generics.
         *
         * @return a collection of child Generic objects
         */
        default Collection<Generic<T>> getChildren(){
            return List.of();
        }
    }
}
