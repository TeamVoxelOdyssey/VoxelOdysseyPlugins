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
package com.guy7cc.voxelodyssey.game.entity;

import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.entity.VOEntity;
import com.guy7cc.voxelodyssey.core.entity.VOEntityFactoryArgs;
import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.game.VOPlugin;
import com.guy7cc.voxelodyssey.game.system.VOElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

/**
 * A class that represents a damage number entity in the game.
 * <p>
 * This class is used to create and manage damage number entities that are displayed in the game world.
 * </p>
 */
public class VODamageNum implements VOEntity<TextDisplay> {
    private static final char BASE_ZERO = '\uE100';

    private TextDisplay handle;
    private int tick = 0;

    protected VODamageNum(FactoryArgs args) {
        String text = Long.toString(Math.round(args.getDamage()));
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append((char) (BASE_ZERO + c - '0' + args.getElement().ordinal() * 10));
        }
        TextDisplay display = args.getLocation().getWorld().spawn(args.getLocation(), TextDisplay.class);
        display.text(Component.text(sb.toString()));
        display.setBillboard(Display.Billboard.VERTICAL);
        display.setTransformation(
                new Transformation(
                        new Vector3f(0F, 1F, 0F),
                        new AxisAngle4f(),
                        new Vector3f(),
                        new AxisAngle4f()
                )
        );
        display.setInterpolationDelay(0);
        display.setInterpolationDuration(15);
        display.setPersistent(false);
        this.handle = display;
    }

    @Override
    public void tick(int globalTick) {
        VOPlugin.getPlugin().getLogger().info("" + tick);
        tick++;
    }

    @Override
    public TextDisplay getHandle() {
        return handle;
    }

    @Override
    public boolean shouldBeRemoved() {
        return tick >= 15;
    }

    public static class FactoryArgs extends VOEntityFactoryArgs {
        private final double damage;
        private final VOElement element;
        private final Location location;

        public FactoryArgs(double damage, VOElement element, Location location) {
            this.damage = damage;
            this.element = element;
            this.location = location;
        }

        public double getDamage() {
            return damage;
        }

        public VOElement getElement() {
            return element;
        }

        public Location getLocation() {
            return location;
        }
    }

    public static class Type extends VOEntityType<VODamageNum, FactoryArgs> {
        public Type() {
            super(Key.vo("damage_num"));
        }

        @Override
        public VODamageNum create(VODamageNum.FactoryArgs args) {
            return new VODamageNum(args);
        }

        @Override
        public VODamageNum wrap(Entity entity, FactoryArgs factoryArgs) {
            throw new UnsupportedOperationException("wrap function is not supported");
        }


    }
}
