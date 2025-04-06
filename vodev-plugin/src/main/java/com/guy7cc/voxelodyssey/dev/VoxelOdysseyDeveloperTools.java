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
package com.guy7cc.voxelodyssey.dev;

import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.command.CommandManager;
import com.guy7cc.voxelodyssey.core.common.GeneralEventHandler;
import com.guy7cc.voxelodyssey.core.common.GeneralPluginLifecycleHandler;
import com.guy7cc.voxelodyssey.core.common.GeneralTicker;
import com.guy7cc.voxelodyssey.core.data.DataLoader;
import com.guy7cc.voxelodyssey.core.data.JsonFileIO;
import com.guy7cc.voxelodyssey.core.registry.RegistryManager;
import com.guy7cc.voxelodyssey.dev.banner.Banners;
import com.guy7cc.voxelodyssey.dev.command.VODevBannerCommand;
import com.guy7cc.voxelodyssey.dev.command.VODevCommand;
import com.guy7cc.voxelodyssey.dev.command.VODevHatCommand;
import com.guy7cc.voxelodyssey.dev.landmark.LandmarkManager;
import com.guy7cc.voxelodyssey.dev.registry.VODevRegistryTypes;
import com.guy7cc.voxelodyssey.dev.tool.ToolManager;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The main class for the VoxelOdysseyDeveloperTools plugin.
 * <p>
 * This class is responsible for setting up the plugin and providing access to various components.
 * </p>
 */
public class VoxelOdysseyDeveloperTools {
    public static final String DATA_VERSION = "v0";

    private static Logger logger;
    private static JsonFileIO json;

    private static GeneralPluginLifecycleHandler lifecycleHandler;
    private static GeneralEventHandler eventHandler;
    private static GeneralTicker ticker;

    private static RegistryManager.Registrar registrar;
    private static CommandManager commandManager;
    private static LandmarkManager landmarkManager;
    private static ToolManager toolManager;

    private static DataLoader dataLoader;

    private VoxelOdysseyDeveloperTools(){

    }

    static void setup(VODevPlugin plugin){
        logger = plugin.getLogger();
        json = new JsonFileIO(logger);

        lifecycleHandler = new GeneralPluginLifecycleHandler(plugin);
        eventHandler = new GeneralEventHandler(plugin);
        ticker = new GeneralTicker(plugin);

        registrar = VoxelOdysseyCore.getRegistryManager().new Registrar(Map.of(
                VODevRegistryTypes.BANNER, Banners.REGISTRY
        ));
        commandManager = new CommandManager(plugin, new VODevCommand(), new VODevHatCommand(), new VODevBannerCommand());
        landmarkManager = new LandmarkManager(plugin);
        toolManager = new ToolManager();
        dataLoader = new DataLoader(plugin, new File(plugin.getDataFolder(), "vodevdat.json"), json, DATA_VERSION);

        dataLoader.collectStaticHolders(VoxelOdysseyDeveloperTools.class);
        lifecycleHandler.collectStatic(VoxelOdysseyDeveloperTools.class);
        eventHandler.collectStatic(VoxelOdysseyDeveloperTools.class);
        ticker.collectStatic(VoxelOdysseyDeveloperTools.class);
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

    public static LandmarkManager getLandmarkManager(){
        return landmarkManager;
    }

    public static ToolManager getToolManager(){
        return toolManager;
    }
}
