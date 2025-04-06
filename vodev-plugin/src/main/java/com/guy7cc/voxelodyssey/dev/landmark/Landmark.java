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
package com.guy7cc.voxelodyssey.dev.landmark;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * A class that represents a landmark in the world.
 */
public class Landmark implements JsonSerializable<Landmark> {
    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Landmark(){
        this("world", 0.0, 0.0, 0.0, 0f, 0f);
    }

    public Landmark(String world, double x, double y, double z, float yaw, float pitch){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Creates a landmark from a location.
     *
     * @param loc the location to create the landmark from
     * @return the landmark
     */
    public static Landmark fromLocation(Location loc){
        return new Landmark(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    /**
     * Converts this landmark to a location.
     *
     * @return the location
     */
    public Location toLocation(){
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    @Override
    public Landmark initialize() {
        this.world = "world";
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        this.yaw = 0f;
        this.pitch = 0f;
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("world", world);
        o.addProperty("x", x);
        o.addProperty("y", y);
        o.addProperty("z", z);
        o.addProperty("yaw", yaw);
        o.addProperty("pitch", pitch);
        return o;
    }

    @Override
    public Landmark fromJson(JsonElement jsonElement) throws DataFormatException {
        try{
            JsonObject o = jsonElement.getAsJsonObject();
            world = o.get("world").getAsString();
            x = o.get("x").getAsDouble();
            y = o.get("y").getAsDouble();
            z = o.get("z").getAsDouble();
            yaw = o.get("yaw").getAsFloat();
            pitch = o.get("pitch").getAsFloat();
            return this;
        } catch(Exception e){
            throw new DataFormatException(getClass(), e);
        }
    }
}
