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
package com.guy7cc.voxelodyssey.game.entity;

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.game.system.VOElement;
import com.guy7cc.voxelodyssey.game.system.VOElementalVector;
import org.bukkit.entity.LivingEntity;

/**
 * An interface for entities that can notify damage.
 * @param <T> the type of the entity
 */
public interface VODamageNotifiable<T extends LivingEntity> extends VODamageable<T> {
    default void notifyDamage(VOElementalVector v){
        for (VOElement element : VOElement.values()) {
            double damage = v.get(element);
            if (damage <= 1e-3) continue;
            VoxelOdysseyCore.spawn(VOEntityTypes.DAMAGE_NUM, new VODamageNum.FactoryArgs(damage, element, getHandle().getEyeLocation()));
        }
    }
}
