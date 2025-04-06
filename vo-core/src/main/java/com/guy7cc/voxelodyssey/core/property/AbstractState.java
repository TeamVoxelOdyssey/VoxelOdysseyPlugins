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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.registry.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Abstract class representing a state with properties.
 * This class implements the State interface and provides methods to manage properties.
 *
 * @param <T> the type of the state
 * @param <U> the type of itself
 */
public abstract class AbstractState<T, U extends AbstractState<T, U>> implements State<T, U>, JsonSerializable<AbstractState<T, U>> {
    private final Map<Property<?>, Object> properties = new HashMap<>();

    @Override
    public final boolean containsProperty(@NotNull Property<?> property) {
        return properties.containsKey(property);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <S> S getProperty(@NotNull Property<S> property) {
        return properties.containsKey(property) ? (S) properties.get(property) : property.defaultValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <S> U setProperty(@NotNull Property<S> property, S value) {
        properties.put(property, value);
        return (U) this;
    }

    @Override
    public AbstractState<T, U> initialize() {
        properties.clear();
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject j = new JsonObject();
        for (var p : properties.keySet()) {
            forProperty(p, p2 -> j.add(p2.getKey().toString(), p2.parseJson(getProperty(p2))));
        }
        return j;
    }

    @Override
    public AbstractState<T, U> fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject obj = j.getAsJsonObject();
            for (String pName : obj.keySet()) {
                Key key = Key.fromString(pName);
                if (!VOCoreProperties.REGISTRY.containsKey(key)) throw new DataFormatException(String.format("Property key %s is invalid", key));
                Property<?> p = VOCoreProperties.REGISTRY.get(key);
                JsonElement element = obj.get(pName);
                setPropertySafe(p, element);
            }
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }

    private <S> void forProperty(Property<S> property, Consumer<Property<S>> consumer) {
        consumer.accept(property);
    }

    private <S> void setPropertySafe(Property<S> property, JsonElement element) throws DataFormatException {
        setProperty(property, property.parseValue(element));
    }
}
