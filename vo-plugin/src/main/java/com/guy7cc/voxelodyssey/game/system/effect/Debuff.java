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

import com.guy7cc.voxelodyssey.core.property.ImmutableValueHolder;
import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import com.guy7cc.voxelodyssey.core.registry.Key;
import net.kyori.adventure.text.Component;

import java.util.Comparator;
import java.util.Optional;

/**
 * Represents a debuff effect that can be applied to an entity.
 * <p>
 * This class is used to create debuff effects that can be applied to entities.
 * </p>
 */
public class Debuff extends VOEffect<ImmutableValueHolder<Double>> {
    private final Component component;

    public Debuff(Key key, Component component) {
        super(key);
        this.component = component;
    }

    @Override
    public VOEffectState getDefaultEffectState() {
        return getState(200, 20, 1D);
    }

    public VOEffectState getState(int length, int interval, double damage) {
        if (length <= 0) throw new IllegalArgumentException("Debuff length must be more than 0");
        if (interval <= 0) throw new IllegalArgumentException("Debuff interval must be more than 0");
        if (damage <= 0) throw new IllegalArgumentException("Debuff damage must be more than 0");
        return new VOEffectState(this)
                .setProperty(VOCoreProperties.LEFT_TICK, length)
                .setProperty(VOCoreProperties.INTERVAL, interval)
                .setProperty(VOCoreProperties.DAMAGE, damage);
    }

    @Override
    public Optional<ImmutableValueHolder<Double>> onTick(VOEffectPipeline.States states, VOEffectApplicable<?> receiver, int tick) {
        if (states.effects.isEmpty()) return Optional.empty();
        var max = states.effects.stream().max(Comparator.comparing(s -> s.getProperty(VOCoreProperties.DAMAGE))).get();
        int leftTick = max.getProperty(VOCoreProperties.LEFT_TICK);
        int interval = max.getProperty(VOCoreProperties.INTERVAL);
        Optional<ImmutableValueHolder<Double>> optional = leftTick % interval == interval - 1
                ? Optional.of(ImmutableValueHolder.of(max.getProperty(VOCoreProperties.DAMAGE)))
                : Optional.empty();
        var iterator = states.effects.iterator();
        while (iterator.hasNext()) {
            var state = iterator.next();
            int lt = state.getProperty(VOCoreProperties.LEFT_TICK);
            lt--;
            if (lt > 0) state.setProperty(VOCoreProperties.LEFT_TICK, lt);
            else iterator.remove();
        }
        return optional;
    }

    @Override
    public void passOnTick(VOEffectPipeline.States states, ImmutableValueHolder<Double> value, ImmutableValueHolder<Double> baseValue, VOModifierState state, VOEffectApplicable<?> receiver, int tick) {
        if (state.containsProperty(VOCoreProperties.MUL)) {
            value.pass(d -> d * state.getProperty(VOCoreProperties.MUL));
        }
        if (state.containsProperty(VOCoreProperties.ADD)) {
            value.pass(d -> d + state.getProperty(VOCoreProperties.ADD));
        }
        value.pass(d -> Math.max(d, 0d));
    }

    @Override
    public void applyOnTick(VOEffectPipeline.States states, ImmutableValueHolder<Double> finalValue, VOEffectApplicable<?> receiver, int tick) {
        receiver.damage(finalValue.value);
    }
}
