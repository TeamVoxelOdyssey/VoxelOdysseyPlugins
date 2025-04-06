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
package com.guy7cc.voxelodyssey.core.entity.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.registry.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * A no-operation implementation of the VOPlayer interface.
 * It wraps a Player object and provides a no-op implementation of the VOPlayer interface.
 */
public final class NoOpVOPlayer implements VOPlayer {
    private final Player handle;

    public NoOpVOPlayer(Player player){
        this.handle = player;
    }

    @Override
    public Player getHandle() {
        return handle;
    }

    @Override
    public VOPlayer initialize() {
        return this;
    }

    @Override
    public JsonElement toJson() {
        return new JsonObject();
    }

    @Override
    public VOPlayer fromJson(JsonElement j) throws DataFormatException {
        return this;
    }

    /**
     * A factory class for create instances of NoOpVOPlayer.
     * It extends the VOEntityType class and provides a method to wrap a Player object.
     */
    public static final class Type extends VOEntityType<VOPlayer, FactoryArgs> {
        public Type() {
            super(Key.vo("voplayer"));
        }

        @Override
        public VOPlayer create(FactoryArgs args) {
            throw new UnsupportedOperationException("Cannot spawn player");
        }

        @Override
        public VOPlayer wrap(Entity entity, FactoryArgs args) {
            if(!(entity instanceof Player player)) return null;
            return new NoOpVOPlayer(player);
        }
    }
}
