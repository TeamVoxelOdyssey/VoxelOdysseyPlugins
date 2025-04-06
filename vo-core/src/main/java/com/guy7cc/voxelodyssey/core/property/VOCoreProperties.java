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

import com.guy7cc.voxelodyssey.core.VOCorePlugin;
import com.guy7cc.voxelodyssey.core.registry.IndexedKey;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.registry.Registry;

import java.util.UUID;

/**
 * This class is responsible for managing the core properties in the VoxelOdyssey game.
 * It provides a registry for Property objects and a method to register new properties.
 */
public class VOCoreProperties {
    public static final Registry<Property<?>> REGISTRY;

    // GENERAL
    public static final Property<Key> OWNER;

    // ITEM PROPERTY
    public static final Property<UUID> UNIQUE;
    public static final Property<Integer> USE;
    public static final Property<Integer> MAX_USE;
    public static final Property<Integer> COOLTIME;
    public static final Property<Key> COOLTIME_KEY;

    // EFFECT PROPERTY
    public static final Property<Integer> LEFT_TICK;
    public static final Property<Integer> INTERVAL;
    public static final Property<Double> DAMAGE;

    // MODIFIER PROPERTY
    public static final Property<IndexedKey> SOURCE;
    public static final Property<Integer> RANK;
    public static final Property<Double> ADD;
    public static final Property<Double> MUL;
    
    static {
        REGISTRY = new Registry<>(VOCorePlugin::getPlugin);

        // GENERAL
        OWNER = register(new KeyProperty(
                Key.vo("owner"),
                Key.vodefault()
        ));


        UNIQUE = register(new UuidProperty(
                Key.vo("unique")
        ));
        USE = register(new IntegerProperty(
                Key.vo("use"),
                0
        ));
        MAX_USE = register(new IntegerProperty(
                Key.vo("max_use"),
                10
        ));
        COOLTIME = register(new IntegerProperty(
                Key.vo("cooltime"),
                100
        ));
        COOLTIME_KEY = register(new KeyProperty(
                Key.vo("cooltime_key"),
                Key.vodefault()
        ));

        // EFFECT PROPERTY
        LEFT_TICK = register(new IntegerProperty(
                Key.vo("left_tick"),
                50
        ));
        INTERVAL = register(new IntegerProperty(
                Key.vo("interval"),
                20
        ));
        DAMAGE = register(new DoubleProperty(
                Key.vo("damage"),
                1d
        ));

        // MODIFIER PROPERTY
        SOURCE = register(new JsonSerializableProperty<>(
                Key.vo("source"),
                IndexedKey::none
        ));
        RANK = register(new IntegerProperty(
                Key.vo("rank"),
                0
        ));
        ADD = register(new DoubleProperty(
                Key.vo("add"),
                0d
        ));
        MUL = register(new DoubleProperty(
                Key.vo("mul"),
                0d
        ));
    }

    private VOCoreProperties() {
        
    }

    private static <T, S extends Property<T>> S register(S object) {
        REGISTRY.register(object);
        return object;
    }
}
