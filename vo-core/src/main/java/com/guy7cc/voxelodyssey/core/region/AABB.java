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
package com.guy7cc.voxelodyssey.core.region;

import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * Axis-Aligned Bounding Box (AABB) class representing a 3D box defined by two opposite corners.
 * Implements the RegionShape interface.
 */
public final class AABB implements RegionShape {
    private final Vector min;
    private final Vector max;

    public AABB(Vector v0, Vector v1){
        min = new Vector(Math.min(v0.getX(), v1.getX()), Math.min(v0.getY(), v1.getY()), Math.min(v0.getZ(), v1.getZ()));
        max = new Vector(Math.max(v0.getX(), v1.getX()), Math.max(v0.getY(), v1.getY()), Math.max(v0.getZ(), v1.getZ()));
    }

    public Vector[] getAllVertices(){
        Vector[] array = new Vector[8];
        for(int i = 0; i < 8; i++){
            Vector v = new Vector();
            v.setX(i % 2 == 0 ? min.getX() : max.getX());
            v.setY((i >> 1) % 2 == 0 ? min.getY() : max.getY());
            v.setZ((i >> 2) % 2 == 0 ? min.getZ() : max.getZ());
            array[i] = v;
        }
        return array;
    }

    @Override
    public boolean contains(Vector v) {
        return v.isInAABB(min, max);
    }

    @Override
    public boolean contains(RegionShape shape) {
        if(shape instanceof AABB other){
            return contains(other.min) && contains(other.max);
        } else if(shape instanceof Sphere sphere){
            Vector c = sphere.getCenter();
            double r = sphere.getRadius();
            return min.getX() <= c.getX() - r && c.getX() + r <= max.getX()
                    && min.getY() <= c.getY() - r && c.getY() + r <= max.getY()
                    && min.getZ() <= c.getZ() - r && c.getZ() + r <= max.getZ();
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof AABB aabb))
            return false;
        return min.equals(aabb.min) && max.equals(aabb.max);
    }

    @Override
    public String toString() {
        return "AABB{min=(" + min.toString() + "),max=(" + max.toString() + ")}";
    }
}
