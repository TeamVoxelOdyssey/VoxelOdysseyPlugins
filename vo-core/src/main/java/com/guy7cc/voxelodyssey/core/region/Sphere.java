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

import com.google.common.base.Preconditions;
import org.bukkit.util.Vector;

import java.util.Objects;

/**
 * Represents a spherical region in 3D space.
 * The sphere is defined by its center and radius.
 */
public final class Sphere implements RegionShape{
    private final Vector c;
    private final double r;

    public Sphere(Vector c, double r){
        Preconditions.checkArgument(r > 0, "Sphere radius must be more than 0");
        this.c = c;
        this.r = r;
    }

    public Vector getCenter(){
        return c.clone();
    }

    public double getRadius(){
        return r;
    }

    @Override
    public boolean contains(Vector v) {
        return v.isInSphere(c, r);
    }

    @Override
    public boolean contains(RegionShape shape) {
        if(shape instanceof Sphere other){
            return contains(other.c)
                    && this.r >= other.r
                    && this.c.distanceSquared(other.c) <= (this.r - other.r) * (this.r - other.r);
        } else if(shape instanceof AABB aabb){
            for(Vector v : aabb.getAllVertices()){
                if(!contains(v)) return false;
            }
            return true;
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, r);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Sphere sphere))
            return false;
        return c.equals(sphere.c) && r == sphere.r;
    }

    @Override
    public String toString() {
        return "Sphere{c=(" + c.toString() + "),r=" + r + "}";
    }
}
