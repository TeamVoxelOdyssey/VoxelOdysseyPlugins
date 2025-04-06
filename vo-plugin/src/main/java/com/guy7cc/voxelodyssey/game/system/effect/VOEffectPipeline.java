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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.common.Copyable;
import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import com.guy7cc.voxelodyssey.core.registry.IndexedKey;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;

import java.util.*;

/**
 * A pipeline for applying and modify effects for a receiver.
 * <p>
 * This class is used to create a pipeline of effects and modifiers that can be applied to a receiver.
 * </p>
 *
 * @param <T> the type of the value being modified
 */
public class VOEffectPipeline<T extends Copyable<T>> implements Tickable, JsonSerializable<VOEffectPipeline<T>> {
    private final VOEffectApplicable<?> receiver;
    private final VOEffect<T> effect;
    private final Set<VOEffectState> eStates = new HashSet<>();
    private final SortedSet<VOModifierState> mStates = new TreeSet<>();
    private final States states = new States(eStates, mStates);

    public VOEffectPipeline(VOEffectApplicable<?> receiver, VOEffect<T> effect) {
        this.receiver = receiver;
        this.effect = effect;
    }

    /**
     * Adds an effect state to the pipeline.
     *
     * @param state the effect state to add
     */
    public void addEffect(VOEffectState state) {
        eStates.add(state);
        Optional<T> optional = effect.onEffectAdded(states, state, receiver);
        if (optional.isPresent()) {
            T baseValue = optional.get();
            T value = baseValue.clone();
            mStates.forEach(r -> effect.passOnEffectAdded(states, value, baseValue, r, receiver));
            effect.applyOnEffectAdded(states, value, receiver);
        }
    }

    /**
     * Adds a modifier state to the pipeline.
     *
     * @param state the modifier state to add
     */
    public void addModifier(VOModifierState state) {
        mStates.add(state);
        Optional<T> optional = effect.onModifierAdded(states, state, receiver);
        if (optional.isPresent()) {
            T baseValue = optional.get();
            T value = baseValue.clone();
            mStates.forEach(m -> effect.passOnModifierAdded(states, value, baseValue, m, receiver));
            effect.applyOnModifierAdded(states, value, receiver);
        }
    }

    /**
     * Removes an effect state from the pipeline by its source.
     *
     * @param source the source of the effect state to remove
     */
    public void clearModifierBySource(IndexedKey source) {
        mStates.removeIf(state -> state.getProperty(VOCoreProperties.SOURCE).equals(source));
    }

    /**
     * Removes all modifier states from the pipeline.
     */
    public void clearModifierAll() {
        mStates.clear();
    }

    @Override
    public void tick(int globalTick) {
        Optional<T> optional = effect.onTick(states, receiver, globalTick);
        if (optional.isPresent()) {
            T baseValue = optional.get();
            T value = baseValue.clone();
            mStates.forEach(modifier -> effect.passOnTick(states, value, baseValue, modifier, receiver, globalTick));
            effect.applyOnTick(states, value, receiver, globalTick);
        }
    }

    @Override
    public VOEffectPipeline<T> initialize() {
        eStates.clear();
        mStates.clear();
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject j = new JsonObject();
        JsonArray eArray = new JsonArray();
        for (var state : eStates) {
            eArray.add(state.toJson());
        }
        j.add("effect_states", eArray);
        JsonArray rArray = new JsonArray();
        for (var state : mStates) {
            rArray.add(state.toJson());
        }
        j.add("resistance_states", rArray);
        return j;
    }

    @Override
    public VOEffectPipeline<T> fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject o = j.getAsJsonObject();
            JsonArray eArray = o.get("effect_states").getAsJsonArray();
            for (JsonElement element : eArray) {
                JsonObject obj = element.getAsJsonObject();
                VOEffectState state = new VOEffectState(effect);
                state.fromJson(obj);
                if (state.getOwner() != effect) throw new DataFormatException("Effect state is not in the right pipeline");
                eStates.add(state);
            }
            JsonArray rArray = o.get("resistance_states").getAsJsonArray();
            for (JsonElement element : rArray) {
                JsonObject obj = element.getAsJsonObject();
                VOModifierState state = new VOModifierState(effect);
                state.fromJson(obj);
                if (state.getOwner() != effect) throw new DataFormatException("Modifier state is not in the right pipeline");
                mStates.add(state);
            }
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }

    public static class States {
        public final Set<VOEffectState> effects;
        public final SortedSet<VOModifierState> modifiers;

        private States(Set<VOEffectState> effects, SortedSet<VOModifierState> modifiers) {
            this.effects = effects;
            this.modifiers = modifiers;
        }
    }
}
