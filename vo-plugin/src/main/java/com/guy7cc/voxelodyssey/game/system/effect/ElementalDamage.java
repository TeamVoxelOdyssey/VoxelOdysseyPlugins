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

import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.game.entity.VODamageNotifiable;
import com.guy7cc.voxelodyssey.game.property.VOProperties;
import com.guy7cc.voxelodyssey.game.system.VOElementalVector;

import java.util.Optional;

/**
 * ElementalDamage class that extends VOEffect.
 * This class is responsible for handling elemental damage effects in the game.
 */
public class ElementalDamage extends VOEffect<VOElementalVector> {
    public ElementalDamage(Key key) {
        super(key);
    }

    @Override
    public VOEffectState getDefaultEffectState() {
        return getState(new VOElementalVector());
    }

    public VOEffectState getState(VOElementalVector v) {
        return new VOEffectState(this)
                .setProperty(VOProperties.ELEMENT, v);
    }

    @Override
    public Optional<VOElementalVector> onEffectAdded(VOEffectPipeline.States states, VOEffectState added, VOEffectApplicable<?> receiver) {
        VOElementalVector v = new VOElementalVector();
        for (var state : states.effects) {
            v.add(state.getProperty(VOProperties.ELEMENT));
        }
        states.effects.clear();
        return Optional.of(v);
    }

    @Override
    public void passOnEffectAdded(VOEffectPipeline.States states, VOElementalVector value, VOElementalVector baseValue, VOModifierState state, VOEffectApplicable<?> receiver) {
        if (state.containsProperty(VOCoreProperties.MUL)) {
            value.mul(state.getProperty(VOProperties.MUL_VECTOR));
        }
        if (state.containsProperty(VOCoreProperties.ADD)) {
            value.add(state.getProperty(VOProperties.ADD_VECTOR));
        }
        value.rectify();
    }

    @Override
    public void applyOnEffectAdded(VOEffectPipeline.States states, VOElementalVector finalValue, VOEffectApplicable<?> receiver) {
        if(receiver instanceof VODamageNotifiable<?> notifiable) notifiable.notifyDamage(finalValue);
        receiver.damageSilent(finalValue.sum());
    }
}
