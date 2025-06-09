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
package com.guy7cc.voxelodyssey.core.command;

import com.guy7cc.voxelodyssey.core.party.Party;
import com.guy7cc.voxelodyssey.core.party.PartyRepository;
import com.guy7cc.voxelodyssey.core.util.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PartyArg implements CommandArg<Party> {
    private final String name;
    private final Supplier<PartyRepository> repoSupplier;

    public PartyArg(String name, Supplier<PartyRepository> repoSupplier) {
        this.name = name;
        this.repoSupplier = repoSupplier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(String arg) {
        PartyRepository repo = repoSupplier.get();
        Party party = repo.fromName(arg);
        if(party != null) return true;
        try{
            party = repo.fromId(UUID.fromString(arg));
            return party != null;
        } catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public Party get(String arg) {
        PartyRepository repo = repoSupplier.get();
        Party party = repo.fromName(arg);
        if(party != null) return party;
        try{
            return repo.fromId(UUID.fromString(arg));
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(String arg) {
        return StringUtil.getOptions(arg, repoSupplier.get().getParties().stream().map(Party::getName).toList());
    }
}
