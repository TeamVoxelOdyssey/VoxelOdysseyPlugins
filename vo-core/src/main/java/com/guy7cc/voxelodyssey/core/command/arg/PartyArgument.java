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
package com.guy7cc.voxelodyssey.core.command.arg;

import com.guy7cc.voxelodyssey.core.party.Party;
import com.guy7cc.voxelodyssey.core.party.PartyRepository;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PartyArgument implements CustomArgumentType.Converted<Party, String> {
    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(id -> {
        return MessageComponentSerializer.message().serialize(Component.text(id + " is not a valid party name or id."));
    });

    private Supplier<PartyRepository> repo;

    private PartyArgument(Supplier<PartyRepository> repo){
        this.repo = repo;
    }

    public static PartyArgument party(Supplier<PartyRepository> repo){
        return new PartyArgument(repo);
    }

    public static Party getParty(CommandContext<?> context, String name){
        return context.getArgument(name, Party.class);
    }

    @Override
    public Party convert(String nativeType) throws CommandSyntaxException {
        Party party = repo.get().fromName(nativeType);
        if(party != null) return party;
        try{
            UUID id = UUID.fromString(nativeType);
            party = repo.get().fromId(id);
            if(party != null) return party;
            else {
                throw ERROR_INVALID.create(nativeType);
            }
        } catch(IllegalArgumentException e){
            throw ERROR_INVALID.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Party party : repo.get().getParties()) {
            String name = party.getName();
            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
