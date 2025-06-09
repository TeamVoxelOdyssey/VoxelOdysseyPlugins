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
import com.guy7cc.voxelodyssey.core.command.arg.PartyArgument;
import com.guy7cc.voxelodyssey.core.party.Party;
import com.guy7cc.voxelodyssey.core.party.PartyOperationResult;
import com.guy7cc.voxelodyssey.core.party.PartyRepository;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PartyCommand {
    private static final LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("party")
            .then(Commands.literal("create")
                    .then(Commands.argument("name", StringArgumentType.string())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> create(ctx.getSource(), StringArgumentType.getString(ctx, "name"), false))
                            .then(Commands.argument("private", BoolArgumentType.bool())
                                    .requires(source -> source.getExecutor() instanceof Player)
                                    .executes(ctx -> create(ctx.getSource(), StringArgumentType.getString(ctx, "name"), BoolArgumentType.getBool(ctx, "private")))
                            )
                    )
            ).then(Commands.literal("requestInvite")
                    .then(Commands.argument("party", PartyArgument.party(VoxelOdysseyCore::getPartyRepository))
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> requestInvite(ctx.getSource(), PartyArgument.getParty(ctx, "party")))
                    )
            ).then(Commands.literal("invite")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> invite(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())))
                    )
            ).then(Commands.literal("join")
                    .then(Commands.argument("party", PartyArgument.party(VoxelOdysseyCore::getPartyRepository))
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> join(ctx.getSource(), PartyArgument.getParty(ctx, "party")))
                    )
            ).then(Commands.literal("leave")
                    .requires(source -> source.getExecutor() instanceof Player)
                    .executes(ctx -> leave(ctx.getSource()))
            ).then(Commands.literal("kick")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> kick(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())))
                    )
            ).then(Commands.literal("ban")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> ban(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())))
                    )
            ).then(Commands.literal("unban")
                    .then(Commands.argument("player", ArgumentTypes.player())
                            .requires(source -> source.getExecutor() instanceof Player)
                            .executes(ctx -> unban(ctx.getSource(), ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource())))
                    )
            ).then(Commands.literal("show")
                    .executes(ctx -> show(ctx.getSource()))
            );

    private PartyCommand(){

    }

    public static void register(JavaPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(builder.build());
        });
    }

    private static int create(CommandSourceStack source, String name, boolean isPrivate){
        Player player = (Player) source.getExecutor();
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party existing = repo.fromName(name);
        if(existing != null){
            existing.leave(player.getUniqueId());
            player.sendMessage(Component.translatable("commands.voxelodyssey.party.left", existing.getName()));
        }
        Party created = repo.create(
                (Player) source.getExecutor(),
                name,
                isPrivate
        );
        player.sendMessage(Component.translatable("commands.voxelodyssey.party.created", created.getName()));
        return Command.SINGLE_SUCCESS;
    }

    private static int requestInvite(CommandSourceStack source, Party party){
        PartyOperationResult result = party.requestInvite(source.getExecutor().getUniqueId());
        if(result == PartyOperationResult.SUCCESS){
            source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.requestedInvite", party.getName()));
            return Command.SINGLE_SUCCESS;
        } else {
            source.getExecutor().sendMessage(result.getMessage());
            return 0;
        }
    }

    private static int invite(CommandSourceStack source, List<Player> players){
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party party = repo.fromOwner(source.getExecutor().getUniqueId());
        List<PartyOperationResult> resultList = players.stream().map(p -> party.invite(p.getUniqueId())).toList();
        boolean success = true;
        for(PartyOperationResult result : resultList){
            if(result == PartyOperationResult.SUCCESS){
                source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.invited", party.getName()));
            } else {
                source.getExecutor().sendMessage(result.getMessage());
                success = false;
            }
        }
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private static int join(CommandSourceStack source, Party party){
        PartyOperationResult result = party.join(source.getExecutor().getUniqueId());
        source.getExecutor().sendMessage(result.getMessage());
        if(result == PartyOperationResult.SUCCESS){
            source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.joined", party.getName()));
            return Command.SINGLE_SUCCESS;
        } else {
            source.getExecutor().sendMessage(result.getMessage());
            return 0;
        }
    }

    private static int leave(CommandSourceStack source){
        Player player = (Player) source.getExecutor();
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party party = repo.fromPlayer(player);
        if(party == null){
            player.sendMessage(PartyOperationResult.NOT_IN_PARTY.getMessage());
            return 0;
        }
        PartyOperationResult result = party.leave(player.getUniqueId());
        if(result == PartyOperationResult.SUCCESS){
            source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.left", party.getName()));
            return Command.SINGLE_SUCCESS;
        } else {
            source.getExecutor().sendMessage(result.getMessage());
            return 0;
        }
    }

    private static int kick(CommandSourceStack source, List<Player> players){
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party party = repo.fromOwner(source.getExecutor().getUniqueId());
        if(party == null){
            source.getExecutor().sendMessage(PartyOperationResult.NOT_OWNER.getMessage());
            return 0;
        }
        List<PartyOperationResult> resultList = players.stream().map(p -> party.kick(p.getUniqueId())).toList();
        boolean success = true;
        for(PartyOperationResult result : resultList){
            if(result == PartyOperationResult.SUCCESS){
                source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.kicked", party.getName()));
            } else {
                source.getExecutor().sendMessage(result.getMessage());
                success = false;
            }
        }
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private static int ban(CommandSourceStack source, List<Player> players){
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party party = repo.fromOwner(source.getExecutor().getUniqueId());
        if(party == null){
            source.getExecutor().sendMessage(PartyOperationResult.NOT_OWNER.getMessage());
            return 0;
        }
        List<PartyOperationResult> resultList = players.stream().map(p -> party.ban(p.getUniqueId())).toList();
        boolean success = true;
        for(PartyOperationResult result : resultList){
            if(result == PartyOperationResult.SUCCESS){
                source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.banned", party.getName()));
            } else {
                source.getExecutor().sendMessage(result.getMessage());
                success = false;
            }
        }
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private static int unban(CommandSourceStack source, List<Player> players){
        PartyRepository repo = VoxelOdysseyCore.getPartyRepository();
        Party party = repo.fromOwner(source.getExecutor().getUniqueId());
        if(party == null){
            source.getExecutor().sendMessage(PartyOperationResult.NOT_OWNER.getMessage());
            return 0;
        }
        List<PartyOperationResult> resultList = players.stream().map(p -> party.unban(p.getUniqueId())).toList();
        boolean success = true;
        for(PartyOperationResult result : resultList){
            if(result == PartyOperationResult.SUCCESS){
                source.getExecutor().sendMessage(Component.translatable("commands.voxelodyssey.party.unbanned", party.getName()));
            } else {
                source.getExecutor().sendMessage(result.getMessage());
                success = false;
            }
        }
        return success ? Command.SINGLE_SUCCESS : 0;
    }

    private static int show(CommandSourceStack source){
        CommandSender sender = source.getSender();
        sender.sendMessage(Component.translatable("commands.voxelodyssey.party.show"));
        for(Party party : VoxelOdysseyCore.getPartyRepository().getParties()){
            sender.sendMessage(Component.translatable("commands.voxelodyssey.party.show", party.getName()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
