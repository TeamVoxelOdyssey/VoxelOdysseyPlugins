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
package com.guy7cc.voxelodyssey.core.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;

import java.util.Objects;

/**
 * Represents a key with an associated index.
 * This class is used to identify specific items or slots in a registry.
 * It implements the JsonSerializable interface for JSON serialization and deserialization.
 */
public class IndexedKey implements JsonSerializable<IndexedKey> {
    public static final Key KEY_INVENTORY = Key.vo("inventory");
    public static final Key KEY_ITEM = Key.vo("item");
    public static final Key KEY_POTENTIAL = Key.vo("potential");

    private Key key;
    private int index;

    protected IndexedKey() {
        initialize();
    }

    protected IndexedKey(Key key, int index) {
        this.key = key;
        this.index = index;
    }

    public static IndexedKey none() {
        return new IndexedKey();
    }

    public static IndexedKey fromInventory(int slot) {
        return new IndexedKey(KEY_INVENTORY, slot);
    }

    public static IndexedKey fromItem() {
        return new IndexedKey(KEY_ITEM, 0);
    }

    public static IndexedKey potential() {
        return new IndexedKey(KEY_POTENTIAL, 0);
    }

    public Key getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof IndexedKey other)) return false;
        return key.equals(other.key) && index == other.index;
    }

    @Override
    public String toString() {
        return "IndexedKey{key='" + key.toString() + "', index=" + index + "}";
    }

    @Override
    public IndexedKey initialize() {
        key = Key.vovoid();
        index = 0;
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject j = new JsonObject();
        j.add("key", key.toJson());
        j.addProperty("index", index);
        return j;
    }

    @Override
    public IndexedKey fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject obj = j.getAsJsonObject();
            key = new Key().fromJson(obj.get("key"));
            index = obj.get("index").getAsInt();
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }
}
