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
package com.guy7cc.voxelodyssey.dev.tool.impl;

import com.guy7cc.voxelodyssey.dev.tool.Tool;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * A tool that creates a desert mountain at the location the player specifies.
 * <p>
 * This tool uses WorldEdit to create a mountain made of sandstone.
 * </p>
 */
public class DesertMountain extends Tool {
    private int size;

    public DesertMountain(int size) {
        super("desert_mountain");
        this.size = size;
    }

    @Override
    public void apply(Player player, Location loc) {
        com.sk89q.worldedit.entity.Player actor = BukkitAdapter.adapt(player);
        LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(actor);
        Vector3 targetPos = new Vector3(loc.getX(), loc.getY(), loc.getZ());
        try (EditSession session = localSession.createEditSession(actor)) {
            try {
                int r = size;
                Vector vdir = new Vector(-Math.sin(player.getEyeLocation().getYaw() * Math.PI / 180f), 0, Math.cos(player.getEyeLocation().getYaw() * Math.PI / 180f));
                Vector hdir = new Vector(vdir.getZ(), 0, -vdir.getX());
                double m = getHeight(session, targetPos.toBlockPoint()) + 1.5;
                double vp = getHeight(session, targetPos.add(vdir.getX() * r, 5d, vdir.getZ() * r).toBlockPoint()) + 1;
                double vn = getHeight(session, targetPos.add(vdir.getX() * -r, 5d, vdir.getZ() * -r).toBlockPoint()) + 1;
                double hp = getHeight(session, targetPos.add(hdir.getX() * r, 5d, hdir.getZ() * r).toBlockPoint()) + 1;
                double hn = getHeight(session, targetPos.add(hdir.getX() * -r, 5d, hdir.getZ() * -r).toBlockPoint()) + 1;
                for (int x = -r; x <= r; x++) {
                    for (int z = -r; z <= r; z++) {
                        if (x * x + z * z > r * r) continue;
                        Vector xz = new Vector(x, 0, z);
                        double v = xz.dot(vdir);
                        double h = xz.dot(hdir);
                        BlockVector3 pos = targetPos.add(x, 0, z).withY(m + (h > 0 ? (hp - m) * h / r : (hn - m) * -h / r) + (vn - m) + (vp - vn) * (v + r) / (2 * r)).toBlockPoint();
                        session.setBlock(pos, BlockTypes.SANDSTONE.getDefaultState());
                        BlockVector3 posAir = pos.add(0, 1, 0);
                        while (posAir.y() <= targetPos.y() + 5) {
                            session.setBlock(posAir, BlockTypes.AIR.getDefaultState());
                            posAir = posAir.add(0, 1, 0);
                        }
                        BlockVector3 posSandstone = pos.add(0, -1, 0);
                        while (posSandstone.y() >= targetPos.y() - 5) {
                            session.setBlock(posSandstone, BlockTypes.SANDSTONE.getDefaultState());
                            posSandstone = posSandstone.add(0, -1, 0);
                        }
                    }
                }
            } catch (MaxChangedBlocksException exception) {
                // do nothing cuz of boring implementation
            }

            localSession.remember(session);
            session.commit();
        }
    }

    private int getHeight(EditSession session, BlockVector3 top) {
        while (session.getBlock(top).getBlockType() == BlockTypes.AIR && top.y() > -64) {
            top = top.add(0, -1, 0);
        }
        return top.y();
    }
}
