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
import com.guy7cc.voxelodyssey.core.registry.Registry;
import com.guy7cc.voxelodyssey.core.util.TranslationUtil;
import com.guy7cc.voxelodyssey.game.VOPlugin;
import net.kyori.adventure.text.Component;

/**
 * A class that holds all the effects in the game.
 * <p>
 *     This class is used to register and retrieve effects.
 * </p>
 */
public class VOEffects {
    public static final Registry<VOEffect<?>> REGISTRY = new Registry<>(VOPlugin::getPlugin);
    public static final NoneEffect NONE = register(new NoneEffect());
    public static final ElementalDamage ELEMENTAL_DAMAGE = register(new ElementalDamage(
            Key.vo("elemental_damage")
    ));
    public static final FieldDamage FIELD_DAMAGE = register(new FieldDamage(
            Key.vo("field_damage")
    ));
    public static final Debuff BURN = register(new Debuff(
            Key.vo("burn"),
            Component.translatable(TranslationUtil.guiKey("burn"))
    ));
    public static final Damage FALL = register(new Damage(
            Key.vo("fall")
    ));

    private VOEffects() {

    }

    private static <T extends Copyable<T>, S extends VOEffect<T>> S register(S object) {
        REGISTRY.register(object);
        return object;
    }
}
