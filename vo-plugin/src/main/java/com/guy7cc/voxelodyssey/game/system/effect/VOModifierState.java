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
package com.guy7cc.voxelodyssey.game.system.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.property.AbstractState;
import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import com.guy7cc.voxelodyssey.core.registry.IndexedKey;
import com.guy7cc.voxelodyssey.core.registry.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the state of a modifier in the game.
 * It holds the properties of the modifier and its effect.
 */
public class VOModifierState extends AbstractState<VOEffect<?>, VOModifierState> implements Comparable<VOModifierState> {
    private VOEffect<?> effect;

    public VOModifierState(VOEffect<?> effect) {
        setOwner(effect);
        setDefaultProperty(VOCoreProperties.SOURCE);
        setDefaultProperty(VOCoreProperties.RANK);
    }

    public VOModifierState(VOEffect<?> effect, IndexedKey source, int rank) {
        setOwner(effect);
        setProperty(VOCoreProperties.SOURCE, source);
        setProperty(VOCoreProperties.RANK, rank);
    }

    @Override
    public VOEffect<?> getOwner() {
        return effect;
    }

    private void setOwner(VOEffect<?> effect) {
        this.effect = effect;
        setProperty(VOCoreProperties.OWNER, effect.getKey());
    }

    @Override
    public int compareTo(@NotNull VOModifierState o) {
        return this.getProperty(VOCoreProperties.RANK) - o.getProperty(VOCoreProperties.RANK);
    }

    @Override
    public VOModifierState initialize() {
        super.initialize();
        setOwner(VOEffects.NONE);
        setDefaultProperty(VOCoreProperties.SOURCE);
        setDefaultProperty(VOCoreProperties.RANK);
        return this;
    }

    public boolean isFrom(Key sourceKey) {
        IndexedKey source = getProperty(VOCoreProperties.SOURCE);
        return source.getKey().equals(sourceKey);
    }

    @Override
    public JsonElement toJson() {
        return super.toJson();
    }

    @Override
    public VOModifierState fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject o = j.getAsJsonObject();
            String key = o.get(VOCoreProperties.OWNER.getKey().toString()).getAsString();
            effect = VOEffects.REGISTRY.get(Key.fromString(key));
            if (effect == null) throw new DataFormatException("Cannot find VOEffect with " + key);
            super.fromJson(j);
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }
}
