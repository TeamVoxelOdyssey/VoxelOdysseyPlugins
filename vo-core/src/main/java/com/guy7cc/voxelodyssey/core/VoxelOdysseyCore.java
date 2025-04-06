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
package com.guy7cc.voxelodyssey.core;

import com.guy7cc.voxelodyssey.core.command.CommandManager;
import com.guy7cc.voxelodyssey.core.command.VOItemCommand;
import com.guy7cc.voxelodyssey.core.common.GeneralEventHandler;
import com.guy7cc.voxelodyssey.core.common.GeneralPluginLifecycleHandler;
import com.guy7cc.voxelodyssey.core.common.GeneralTicker;
import com.guy7cc.voxelodyssey.core.common.ScoreboardContainer;
import com.guy7cc.voxelodyssey.core.data.JsonFileIO;
import com.guy7cc.voxelodyssey.core.data.DataLoader;
import com.guy7cc.voxelodyssey.core.entity.*;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import com.guy7cc.voxelodyssey.core.region.RegionManager;
import com.guy7cc.voxelodyssey.core.registry.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.logging.Logger;

/*
 * VoxelOdysseyCore is the core class for the VoxelOdysseyCore plugin.
 * It provides access to various components of the plugin, including
 * lifecycle management, event handling, entity management, and data loading.
 */
public final class VoxelOdysseyCore {
    public static final String NAMESPACE = "voxelodyssey";
    public static final String DATA_VERSION = "v0";

    private static Logger logger;
    private static JsonFileIO json;

    private static GeneralPluginLifecycleHandler lifecycleHandler;
    private static GeneralEventHandler eventHandler;
    private static GeneralTicker ticker;

    private static RegistryManager registryManager;
    private static CommandManager command;
    private static ScoreboardContainer sbContainer;
    private static VOEntityManager eManager;
    private static RegionManager rManager;
    private static DataLoader dataLoader;

    private VoxelOdysseyCore(){

    }

    static void setup(VOCorePlugin plugin){
        logger = plugin.getLogger();
        json = new JsonFileIO(logger);

        lifecycleHandler = new GeneralPluginLifecycleHandler(plugin);
        eventHandler = new GeneralEventHandler(plugin);
        ticker = new GeneralTicker(plugin);

        registryManager = new RegistryManager(plugin);
        command = new CommandManager(plugin, new VOItemCommand());
        sbContainer = new ScoreboardContainer(plugin);
        eManager = new VOEntityManager(plugin);
        rManager = new RegionManager();
        dataLoader = new DataLoader(plugin, new File(plugin.getDataFolder(), "vodat.json"), json, DATA_VERSION);

        dataLoader.collectStaticHolders(VoxelOdysseyCore.class);
        lifecycleHandler.collectStatic(VoxelOdysseyCore.class);
        eventHandler.collectStatic(VoxelOdysseyCore.class);
        ticker.collectStatic(VoxelOdysseyCore.class);
    }

    public static GeneralPluginLifecycleHandler getLifecycleHandler(){
        return lifecycleHandler;
    }

    public static GeneralEventHandler getEventHandler(){
        return eventHandler;
    }

    public static GeneralTicker getTicker(){
        return ticker;
    }

    public static boolean scoreboardInitialized(){
        return sbContainer.isInitialized();
    }

    public static Scoreboard getScoreboard(Player player){
        return sbContainer.getScoreboard(player);
    }

    public static Logger getLogger(){
        return logger;
    }

    public static VOEntity<?> getVOEntity(Entity entity){
        return eManager.get(entity);
    }

    public static VOPlayer getVOPlayer(Player player){
        return eManager.getVOPlayer(player);
    }

    public static <T extends VOEntity<?>, S extends VOEntityFactoryArgs> T spawn(VOEntityType<T, S> type, S args) {
        return eManager.spawn(type, args);
    }

    public static RegistryManager getRegistryManager(){
        return registryManager;
    }

    public static <T extends RegistryObject> Registry<T> addRegistry(RegistryType<T> type, Registry<T> registry){
        return VoxelOdysseyCore.registryManager.add(type, registry);
    }

    public static <T extends RegistryObject> Registry<T> getRegistry(RegistryType<T> type){
        return VoxelOdysseyCore.registryManager.get(type);
    }
}
