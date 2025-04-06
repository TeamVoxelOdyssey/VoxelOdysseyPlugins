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

import com.guy7cc.voxelodyssey.core.entity.VOEntity;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

/**
 * An interface for entities that can take damage.
 * <p>
 * This interface is used to define the behavior of entities that can take damage.
 * </p>
 *
 * @param <T> the type of the living entity
 */
public interface VODamageable<T extends LivingEntity> extends VOEntity<T> {
    double getHp();

    void setHp(double hp);

    double getMaxHp();

    void setMaxHp(double maxHp);

    default void damage(double damage){
        setHp(Math.max(0, getHp()) - damage);
        applyHp();
    }

    default void damageSilent(double damage){
        setHp(Math.max(0, getHp()) - damage);
        applyHpSilent();
    }

    default void applyHp() {
        LivingEntity handle = getHandle();
        double hp = getHp();
        double maxHp = getMaxHp();
        double currentMaxHp = handle.getAttribute(Attribute.MAX_HEALTH).getValue();
        double displayHp = currentMaxHp * hp / maxHp;
        if (displayHp < handle.getHealth()) {
            handle.damage(handle.getHealth() - displayHp);
        } else if (displayHp > handle.getHealth()) {
            handle.heal(displayHp - handle.getHealth());
        }
    }

    default void applyHpSilent() {
        LivingEntity handle = getHandle();
        double hp = getHp();
        double maxHp = getMaxHp();
        double currentMaxHp = handle.getAttribute(Attribute.MAX_HEALTH).getValue();
        double displayHp = currentMaxHp * hp / maxHp;
        handle.setHealth(displayHp);
    }
}
