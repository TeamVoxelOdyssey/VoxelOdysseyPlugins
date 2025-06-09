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
package com.guy7cc.voxelodyssey.core.party;

import com.mojang.brigadier.Command;
import net.kyori.adventure.text.Component;

public enum PartyOperationResult {
    SUCCESS,
    ALREADY_INVITE_REQUESTED,
    ALREADY_INVITED,
    ALREADY_IN_PARTY,
    NOT_IN_PARTY,
    NOT_OWNER,
    IS_PRIVATE,
    BANNED,
    ALREADY_BANNED,
    NOT_BANNED,
    ;

    public Component getMessage(){
        return Component.translatable("commands.voxelodyssey.party." + name().toLowerCase());
    }
}
