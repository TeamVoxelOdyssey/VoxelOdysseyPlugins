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
package com.guy7cc.voxelodyssey.core.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.common.Tickable;
import com.guy7cc.voxelodyssey.core.data.JsonSerializable;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.util.TranslationUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * CoolDown class manages the cooldowns for items in a game.
 * It implements Tickable and JsonSerializable interfaces.
 * The class provides methods to use items, check availability, and manage cooldowns.
 */
public class CoolDown implements Tickable, JsonSerializable<CoolDown> {
    private int lastTick = 0;
    private final Map<Key, Integer> keyToTime = new HashMap<>();
    private final Map<Integer, Set<Key>> timeToKey = new HashMap<>();

    public CoolDown() {
        super();
    }

    @Override
    public void tick(int globalTick) {
        lastTick = globalTick;
        remove(globalTick);
    }

    /**
     * Use an item and set its cooldown.
     *
     * @param itemStack the item to use
     * @param cooldown  the cooldown time in ticks
     */
    public void useItem(ItemStack itemStack, int cooldown) {
        remove(itemStack);
        put(itemStack, lastTick + cooldown);
    }

    /**
     * Check if the item is available.
     *
     * @param itemStack the item to check
     * @return true if the item is available, false otherwise
     */
    public boolean available(ItemStack itemStack){
        return !contains(itemStack) || get(itemStack) >= lastTick;
    }

    /**
     * Check if the player should receive a message.
     *
     * @param player the player to check
     * @return true if the player should receive a message, false otherwise
     */
    public boolean shouldSendMessage(@NotNull Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        return contains(itemStack);
    }

    /**
     * Get the cooldown message for the action bar.
     *
     * @param player the player to get the message for
     * @return the cooldown message
     */
    @Nullable
    public Component getPaperComponentForActionBar(@NotNull Player player){
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (contains(itemStack)) {
            int endTime = get(itemStack);
            if (lastTick < endTime) {
                TextComponent sec = Component.text(String.format("%.1f", (endTime - lastTick) / 20f));
                return Component.translatable(TranslationUtil.guiKey("cooldown", "unavailable"), sec)
                        .color(TextColor.color(0xAAAAAA));
            } else {
                return TranslationUtil.gui("cooldown", "available")
                        .color(TextColor.color(0xAAAAAA));
            }
        }
        return null;
    }

    private boolean contains(ItemStack itemStack) {
        VOItemStackWrapper state = new VOItemStackWrapper(itemStack);
        Key key = state.getCoolDownKey();
        return keyToTime.containsKey(key);
    }

    private boolean contains(int time) {
        return timeToKey.containsKey(time);
    }

    /**
     * Get the cooldown time for the item.
     *
     * @param itemStack the item to get the cooldown for
     * @return the cooldown time in ticks
     */
    public int get(ItemStack itemStack) {
        VOItemStackWrapper state = new VOItemStackWrapper(itemStack);
        Key key = state.getCoolDownKey();
        return keyToTime.get(key);
    }

    private Set<Key> get(int time) {
        return timeToKey.get(time);
    }

    private void put(ItemStack itemStack, int time) {
        VOItemStackWrapper state = new VOItemStackWrapper(itemStack);
        Key key = state.getCoolDownKey();
        put(key, time);
    }

    private void put(Key key, int time) {
        keyToTime.put(key, time);
        if (!timeToKey.containsKey(time)) timeToKey.put(time, new HashSet<>());
        timeToKey.get(time).add(key);
    }

    private void remove(ItemStack itemStack) {
        VOItemStackWrapper state = new VOItemStackWrapper(itemStack);
        Key key = state.getCoolDownKey();
        int time = keyToTime.remove(key);
        Set<Key> set = timeToKey.get(time);
        if (set != null) set.remove(key);
    }

    private void remove(int time) {
        Set<Key> set = timeToKey.remove(time);
        if (set == null) return;
        for (Key key : set) keyToTime.remove(key);
    }

    @Override
    public CoolDown initialize() {
        lastTick = 0;
        keyToTime.clear();
        timeToKey.clear();
        return this;
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Map.Entry<Integer, Set<Key>> entry : timeToKey.entrySet()) {
            JsonArray array = new JsonArray();
            for (Key key : entry.getValue()) {
                array.add(key.toString());
            }
            object.add(String.valueOf(entry.getKey() - lastTick), array);
        }
        return object;
    }

    @Override
    public CoolDown fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject obj = j.getAsJsonObject();
            for (var entry : obj.entrySet()) {
                int time = Integer.parseInt(entry.getKey());
                var array = entry.getValue().getAsJsonArray();
                for (JsonElement element : array) {
                    Key key = Key.fromString(element.getAsString());
                    put(key, time);
                }
            }
            return this;
        } catch (Exception exception) {
            throw new DataFormatException(getClass(), exception);
        }
    }
}
