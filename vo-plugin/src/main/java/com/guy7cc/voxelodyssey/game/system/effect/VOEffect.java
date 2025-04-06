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

import com.guy7cc.voxelodyssey.core.common.Copyable;
import com.guy7cc.voxelodyssey.core.registry.AbstractRegistryObject;
import com.guy7cc.voxelodyssey.core.registry.Key;

import java.util.Optional;

/**
 * A class that represents an effect in the game.
 * <p>
 *     This class is used to create effects that can be applied to entities.
 * </p>
 *
 * @param <T> the type of the effect
 */
public class VOEffect<T extends Copyable<T>> extends AbstractRegistryObject {
    public VOEffect(Key key) {
        super(key);
    }

    /**
     * Creates a new effect state for this effect.
     *
     * @return a new effect state for this effect
     */
    public VOEffectState getDefaultEffectState() {
        return new VOEffectState(this);
    }

    /**
     * Creates a new modifier state for this effect.
     *
     * @return a new modifier state for this effect
     */
    public VOModifierState getDefaultModifierState() {
        return new VOModifierState(this);
    }

    /**
     * Create a new effect value when the effect is added to the receiver.
     *
     * @param states states of the effect pipeline
     * @param added the effect state that was added
     * @param receiver the receiver of the effect
     * @return an optional value that can be applied to the receiver
     */
    public Optional<T> onEffectAdded(VOEffectPipeline.States states, VOEffectState added, VOEffectApplicable<?> receiver) {
        return Optional.empty();
    }

    /**
     * Apply modifier on the effect value created by {@link #onEffectAdded(VOEffectPipeline.States, VOEffectState, VOEffectApplicable)}.
     *
     * @param states states of the effect pipeline
     * @param value the value of the effect
     * @param baseValue the base value of the effect
     * @param state the state of the effect
     * @param receiver the receiver of the effect
     */
    public void passOnEffectAdded(VOEffectPipeline.States states, T value, T baseValue, VOModifierState state, VOEffectApplicable<?> receiver) {

    }

    /**
     * Apply the effect value on the receiver created by {@link #onEffectAdded(VOEffectPipeline.States, VOEffectState, VOEffectApplicable)}.
     *
     * @param states states of the effect pipeline
     * @param finalValue the final value of the effect
     * @param receiver the receiver of the effect
     */
    public void applyOnEffectAdded(VOEffectPipeline.States states, T finalValue, VOEffectApplicable<?> receiver) {

    }

    /**
     * Create a new effect value when the modifier is added to the receiver.
     *
     * @param states states of the effect pipeline
     * @param added the modifier state that was added
     * @param receiver the receiver of the effect
     * @return an optional value that can be applied to the receiver
     */
    public Optional<T> onModifierAdded(VOEffectPipeline.States states, VOModifierState added, VOEffectApplicable<?> receiver) {
        return Optional.empty();
    }

    /**
     * Apply modifier on the effect value created by {@link #onModifierAdded(VOEffectPipeline.States, VOModifierState, VOEffectApplicable)}.
     *
     * @param states states of the effect pipeline
     * @param value the value of the effect
     * @param baseValue the base value of the effect
     * @param state the state of the effect
     * @param receiver the receiver of the effect
     */
    public void passOnModifierAdded(VOEffectPipeline.States states, T value, T baseValue, VOModifierState state, VOEffectApplicable<?> receiver) {

    }

    /**
     * Apply the effect value on the receiver created by {@link #onModifierAdded(VOEffectPipeline.States, VOModifierState, VOEffectApplicable)}.
     *
     * @param states states of the effect pipeline
     * @param finalValue the final value of the effect
     * @param receiver the receiver of the effect
     */
    public void applyOnModifierAdded(VOEffectPipeline.States states, T finalValue, VOEffectApplicable<?> receiver) {

    }

    /**
     * Create a new effect value when the effect is ticked.
     *
     * @param states states of the effect pipeline
     * @param receiver the receiver of the effect
     * @param tick the tick count
     * @return an optional value that can be applied to the receiver
     */
    public Optional<T> onTick(VOEffectPipeline.States states, VOEffectApplicable<?> receiver, int tick) {
        return Optional.empty();
    }

    /**
     * Apply modifier on the effect value created by {@link #onTick(VOEffectPipeline.States, VOEffectApplicable, int)}.
     *
     * @param states states of the effect pipeline
     * @param value the value of the effect
     * @param baseValue the base value of the effect
     * @param state the state of the effect
     * @param receiver the receiver of the effect
     * @param tick the tick count
     */
    public void passOnTick(VOEffectPipeline.States states, T value, T baseValue, VOModifierState state, VOEffectApplicable<?> receiver, int tick) {

    }

    /**
     * Apply the effect value on the receiver created by {@link #onTick(VOEffectPipeline.States, VOEffectApplicable, int)}.
     *
     * @param states states of the effect pipeline
     * @param finalValue the final value of the effect
     * @param receiver the receiver of the effect
     * @param tick the tick count
     */
    public void applyOnTick(VOEffectPipeline.States states, T finalValue, VOEffectApplicable<?> receiver, int tick) {

    }
}
