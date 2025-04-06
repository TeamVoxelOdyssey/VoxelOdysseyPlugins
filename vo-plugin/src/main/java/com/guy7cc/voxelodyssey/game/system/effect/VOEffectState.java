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
import com.guy7cc.voxelodyssey.core.registry.Key;

/**
 * Represents the state of a VOEffect.
 * It is used to store the effect's properties and its owner.
 */
public class VOEffectState extends AbstractState<VOEffect<?>, VOEffectState> {
    private VOEffect<?> effect;

    public VOEffectState(VOEffect<?> effect) {
        setOwner(effect);
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
    public VOEffectState initialize() {
        super.initialize();
        setOwner(VOEffects.NONE);
        return this;
    }

    @Override
    public JsonElement toJson() {
        return super.toJson();
    }

    @Override
    public VOEffectState fromJson(JsonElement j) throws DataFormatException {
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
