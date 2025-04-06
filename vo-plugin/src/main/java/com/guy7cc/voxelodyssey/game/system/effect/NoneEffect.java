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
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.game.VOPlugin;

import java.util.Optional;
import java.util.logging.Level;

/**
 * A class representing a "none" effect in the VoxelOdyssey game.
 * This effect clears all other effects and modifiers when applied.
 */
public class NoneEffect extends VOEffect<NoneEffect.None> {
    public NoneEffect() {
        super(Key.vo("none"));
    }

    @Override
    public Optional<NoneEffect.None> onEffectAdded(VOEffectPipeline.States states, VOEffectState added, VOEffectApplicable<?> receiver) {
        states.effects.clear();
        return Optional.empty();
    }

    @Override
    public Optional<NoneEffect.None> onModifierAdded(VOEffectPipeline.States states, VOModifierState added, VOEffectApplicable<?> receiver) {
        states.modifiers.clear();
        return Optional.empty();
    }

    public static class None implements Copyable<None> {
        @Override
        public None clone() {
            try {
                return (None) super.clone();
            } catch (CloneNotSupportedException exception) {
                VOPlugin.getPlugin().getLogger().log(
                        Level.SEVERE,
                        "Failed to clone NoneEffect.None",
                        exception
                );
            }
            return null;
        }
    }
}
