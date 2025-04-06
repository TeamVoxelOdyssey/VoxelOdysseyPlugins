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
import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.registry.IndexedKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A router for VOEffectPipeline.
 * <p>
 *     This class is used to route effects and modifiers to the correct pipeline.
 * </p>
 */
public class VOEffectRouter implements Tickable, JsonSerializable<VOEffectRouter> {
    private final VOEffectApplicable<?> receiver;
    private final Map<VOEffect<?>, VOEffectPipeline<?>> router = new HashMap<>();

    public VOEffectRouter(VOEffectApplicable<?> receiver) {
        this.receiver = receiver;
        initialize();
    }

    /**
     * Add an effect to the router.
     *
     * @param state the effect to add
     */
    public void addEffect(VOEffectState state) {
        router.get(state.getOwner()).addEffect(state);
    }

    /**
     * Add a collection of effects to the router.
     *
     * @param collection the collection of effects to add
     */
    public void addEffect(Collection<VOEffectState> collection) {
        collection.forEach(this::addEffect);
    }

    /**
     * Add a modifier to the router.
     *
     * @param state the modifier to add
     */
    public void addModifier(VOModifierState state) {
        router.get(state.getOwner()).addModifier(state);
    }

    /**
     * Add a collection of modifiers to the router.
     *
     * @param collection the collection of modifiers to add
     */
    public void addModifier(Collection<VOModifierState> collection) {
        collection.forEach(this::addModifier);
    }

    /**
     * Clear the modifier from the inventory item.
     *
     * @param slot the slot of the inventory item
     */
    public void clearModifierFromInventory(int slot) {
        clearModifier(IndexedKey.fromInventory(slot));
    }

    /**
     * Clear the modifier from the source.
     *
     * @param source the source of the modifier
     */
    public void clearModifier(IndexedKey source) {
        for (var pipeline : router.values()) {
            pipeline.clearModifierBySource(source);
        }
    }

    /**
     * Clear all modifiers from all pipelines.
     */
    public void clearModifierAll() {
        for (var pipeline : router.values()) {
            pipeline.clearModifierAll();
        }
    }

    @Override
    public Collection<? extends Tickable> getTickables() {
        return router.values();
    }

    @Override
    public VOEffectRouter initialize() {
        router.clear();
        VOEffects.REGISTRY.objects().forEach(effect -> router.put(effect, new VOEffectPipeline<>(receiver, effect)));
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject j = new JsonObject();
        for (var entry : router.entrySet()) {
            j.add(entry.getKey().getKey().toString(), entry.getValue().toJson());
        }
        return j;
    }

    @Override
    public VOEffectRouter fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject o = j.getAsJsonObject();
            for (VOEffect<?> effect : router.keySet()) {
                JsonObject obj = o.get(effect.getKey().toString()).getAsJsonObject();
                VOEffectPipeline<?> pipeline = new VOEffectPipeline<>(receiver, effect);
                pipeline.fromJson(obj);
                router.put(effect, pipeline);
            }
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }
}
