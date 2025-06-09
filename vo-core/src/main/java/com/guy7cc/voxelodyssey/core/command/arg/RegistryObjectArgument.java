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
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.registry.Registry;
import com.guy7cc.voxelodyssey.core.registry.RegistryObject;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RegistryObjectArgument<T extends RegistryObject> implements CustomArgumentType.Converted<T, String> {
    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(id -> {
        return MessageComponentSerializer.message().serialize(Component.text(id + " is not a valid key."));
    });

    private Registry<T> registry;

    public RegistryObjectArgument(Registry<T> registry){
        this.registry = registry;
    }

    @Override
    public T convert(String nativeType) throws CommandSyntaxException {
        try{
            Key key = Key.fromString(nativeType);
            if(registry.containsKey(key)){
                return registry.get(key);
            } else {
                throw ERROR_INVALID.create(nativeType);
            }
        } catch(IllegalArgumentException e){
            throw ERROR_INVALID.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (T obj : registry.objects()) {
            String key = obj.getKey().toString();
            if (key.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(key);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}