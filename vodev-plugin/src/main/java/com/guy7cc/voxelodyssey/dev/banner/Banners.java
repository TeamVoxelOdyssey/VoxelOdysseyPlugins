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
package com.guy7cc.voxelodyssey.dev.banner;

import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.registry.Registry;
import com.guy7cc.voxelodyssey.dev.VODevPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * A class that holds all the banners in the game.
 * <p>
 * This class is used to register and retrieve banners.
 * </p>
 */
public class Banners {
    public static final Registry<BannerFactory> REGISTRY = new Registry<>(VODevPlugin::getPlugin);
    public static final BannerFactory WORLD2_UNDERGROUND = register(new BannerFactory(
            Key.vo("world2_underground"), () -> {
                ItemStack is = new ItemStack(Material.RED_BANNER);
                BannerMeta meta = (BannerMeta) is.getItemMeta();
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS));
                meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.TRIANGLE_TOP));
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.TRIANGLE_BOTTOM));
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.GRADIENT));
                meta.addPattern(new Pattern(DyeColor.GRAY, PatternType.FLOWER));
                meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
                is.setItemMeta(meta);
                return is;
            }
    ));

    private Banners(){

    }

    private static BannerFactory register(BannerFactory factory){
        REGISTRY.register(factory);
        return factory;
    }
}
