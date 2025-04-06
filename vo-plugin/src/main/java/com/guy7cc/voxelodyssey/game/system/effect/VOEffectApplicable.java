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

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.entity.VOEntity;
import com.guy7cc.voxelodyssey.game.entity.VODamageable;
import com.guy7cc.voxelodyssey.game.system.VOElement;
import com.guy7cc.voxelodyssey.game.system.VOElementalVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Map;

/**
 * An interface for entities that can apply effects.
 * <p>
 * This interface is used to create entities that can apply effects in the game.
 * </p>
 *
 * @param <T> the type of the entity
 */
public interface VOEffectApplicable<T extends LivingEntity> extends VODamageable<T> {
    /**
     * Gets the effect router for this entity.
     *
     * @return the effect router for this entity
     */
    VOEffectRouter getEffectRouter();

    @EventHandler
    default void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
            VOEffectRouter effectManager = getEffectRouter();
            double damage = event.getDamage();
            event.setDamage(0);
            switch (event.getCause()) {
                case ENTITY_ATTACK:
                case ENTITY_EXPLOSION:
                case ENTITY_SWEEP_ATTACK:
                case PROJECTILE:
                    VOEntity<? extends Entity> wrapper = VoxelOdysseyCore.getVOEntity(event.getDamageSource().getDirectEntity());
                    if (wrapper instanceof VOEffectSender sender) {
                        effectManager.addEffect(sender.getEffectStates());
                    } else {
                        effectManager.addEffect(VOEffects.ELEMENTAL_DAMAGE.getState(new VOElementalVector(Map.of(VOElement.BASE, damage))));
                    }
                    break;
                case KILL:
                case WORLD_BORDER:
                case CONTACT:
                case SUFFOCATION:
                case BLOCK_EXPLOSION:
                case MAGIC:
                case FALLING_BLOCK:
                case THORNS:
                case FLY_INTO_WALL:
                case CRAMMING:
                case DRYOUT:
                case SONIC_BOOM:
                case SUICIDE:
                    effectManager.addEffect(VOEffects.ELEMENTAL_DAMAGE.getState(new VOElementalVector(Map.of(VOElement.BASE, damage))));
                    break;
                case FIRE:
                case FIRE_TICK:
                case MELTING:
                case LAVA:
                case HOT_FLOOR:
                case CAMPFIRE:
                    event.setCancelled(true);
                    effectManager.addEffect(VOEffects.FIELD_DAMAGE.getEffectState(new VOElementalVector(Map.of(VOElement.FLAME, damage))));
                    break;
                case FREEZE:
                    event.setCancelled(true);
                    effectManager.addEffect(VOEffects.FIELD_DAMAGE.getEffectState(new VOElementalVector(Map.of(VOElement.FROST, damage))));
                    break;
                case LIGHTNING:
                    effectManager.addEffect(VOEffects.ELEMENTAL_DAMAGE.getState(new VOElementalVector(Map.of(VOElement.LIGHTNING, damage))));
                    break;
                case POISON:
                    event.setCancelled(true);
                    effectManager.addEffect(VOEffects.FIELD_DAMAGE.getEffectState(new VOElementalVector(Map.of(VOElement.VENOM, damage))));
                    break;
                case WITHER:
                    event.setCancelled(true);
                    effectManager.addEffect(VOEffects.FIELD_DAMAGE.getEffectState(new VOElementalVector(Map.of(VOElement.WITHER, damage))));
                    break;
                case FALL:
                    effectManager.addEffect(VOEffects.FALL.getEffectState(damage));
                    break;
            }
        }
    }

    @EventHandler
    default void onEntityRegainHealth(EntityRegainHealthEvent event) {
        if (event.getRegainReason() != EntityRegainHealthEvent.RegainReason.CUSTOM) {
            double currentHp = getHp();
            VOEffectRouter effectManager = getEffectRouter();
            double amount = event.getAmount();
            event.setAmount(0d);
            if (currentHp == getHp()) event.setCancelled(true);
        }
    }
}
