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

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.party.PartyOperationResult;
import com.guy7cc.voxelodyssey.core.party.PartyManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PartyCommand {
    private static final LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("party")
            .then(Commands.literal("request")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> request(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst()))
                    )
            ).then(Commands.literal("invite")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> invite(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst()))
                    )
            ).then(Commands.literal("join")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> join(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst()))
                    )
            ).then(Commands.literal("leave")
                    .requires(source -> source.getExecutor() instanceof Player)
                    .executes(ctx -> leave(ctx.getSource()))
            ).then(Commands.literal("kick")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> kick(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst()))
                    )
            ).then(Commands.literal("show")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .executes(ctx -> show(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst()))
                    )
            )
            .then(Commands.literal("showAll")
                    .executes(ctx -> showAll(ctx.getSource()))
            );

    private PartyCommand(){

    }

    public static void register(JavaPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(builder.build());
        });
    }

    private static int request(CommandSourceStack source, Player target){
        if(!(source.getExecutor() instanceof Player player)) return 0;
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        PartyOperationResult result = manager.request(player, target);
        return result == PartyOperationResult.SUCCESS ? Command.SINGLE_SUCCESS : 0;
    }

    private static int invite(CommandSourceStack source, Player target){
        if(!(source.getExecutor() instanceof Player player)) return 0;
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        PartyOperationResult result = manager.invite(player, target);
        return result == PartyOperationResult.SUCCESS ? Command.SINGLE_SUCCESS : 0;
    }

    private static int join(CommandSourceStack source, Player target) {
        if(!(source.getExecutor() instanceof Player player)) return 0;
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        PartyOperationResult result = manager.join(player, target);
        return result == PartyOperationResult.SUCCESS ? Command.SINGLE_SUCCESS : 0;
    }

    private static int leave(CommandSourceStack source){
        if(!(source.getExecutor() instanceof Player player)) return 0;
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        PartyOperationResult result = manager.leave(player);
        return result == PartyOperationResult.SUCCESS ? Command.SINGLE_SUCCESS : 0;
    }

    private static int kick(CommandSourceStack source, Player target){
        if(!(source.getExecutor() instanceof Player player)) return 0;
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        PartyOperationResult result = manager.kick(player, target);
        return result == PartyOperationResult.SUCCESS ? Command.SINGLE_SUCCESS : 0;
    }

    private static int show(CommandSourceStack source, Player target) {
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        List<Player> party = manager.getParty(target);
        String str = String.join(", ", party.stream().map(Player::getName).toList());
        source.getSender().sendMessage(Component.translatable("commands.voxelodyssey.party.show")
                .appendSpace()
                .append(Component.text(str))
        );
        return Command.SINGLE_SUCCESS;
    }

    private static int showAll(CommandSourceStack source) {
        PartyManager manager = VoxelOdysseyCore.getPartyManager();
        source.getSender().sendMessage(Component.translatable("commands.voxelodyssey.party.show_all"));
        List<List<Player>> parties = manager.getAllParties();
        for (List<Player> party : parties){
            String str = String.join(", ", party.stream().map(Player::getName).toList());
            source.getSender().sendMessage(Component.text(str));
        }
        return Command.SINGLE_SUCCESS;
    }
}
