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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.guy7cc.voxelodyssey.core.VoxelOdysseyCore;
import com.guy7cc.voxelodyssey.core.common.AbstractWrapper;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import com.guy7cc.voxelodyssey.core.property.Property;
import com.guy7cc.voxelodyssey.core.property.State;
import com.guy7cc.voxelodyssey.core.property.VOCoreProperties;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.util.TranslationUtil;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Wrapper class for ItemStack that provides additional functionality
 * specific to VoxelOdyssey items.
 */
public class VOItemStackWrapper extends AbstractWrapper<ItemStack> implements State<VOItem, VOItemStackWrapper> {
    protected VOItemStackWrapper(ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Creates a new VOItemStackWrapper instance from an ItemStack.
     *
     * @param itemStack the ItemStack to wrap
     * @return a new VOItemStackWrapper instance
     */
    public static VOItemStackWrapper of(ItemStack itemStack) {
        return new VOItemStackWrapper(itemStack);
    }

    @Override
    public VOItem getOwner() {
        Key itemKey = getProperty(VOCoreProperties.OWNER);
        return VOCoreItems.REGISTRY.get(itemKey);
    }

    /**
     * Sets the owner of this item stack to the specified VOItem.
     *
     * @param item the VOItem to set as the owner
     */
    public void setOwner(VOItem item) {
        setProperty(VOCoreProperties.OWNER, item.getKey());
    }

    /**
     * Checks if this item stack is a VOItem.
     *
     * @return true if this item stack is a VOItem, false otherwise
     */
    public boolean isVOItem() {
        return getOwner() != null;
    }

    /**
     * Updates the name of this item stack based on its properties.
     * This includes setting the custom name and use count.
     */
    public void updateName() {
        TranslatableComponent name = Component
                .translatable(TranslationUtil.itemKey(getProperty(VOCoreProperties.OWNER)))
                .decorate(TextDecoration.ITALIC);
        if (containsProperty(VOCoreProperties.USE) && containsProperty(VOCoreProperties.MAX_USE)) {
            int use = getProperty(VOCoreProperties.USE);
            int maxUse = getProperty(VOCoreProperties.MAX_USE);
            TextColor color;
            if (use > 3 && use > maxUse * 3 / 10) color = TextColor.color(0x55FFFF);
            else if (use > 1 && use > maxUse / 10) color = TextColor.color(0x55FF55);
            else color = TextColor.color(0xFF5555);
            TextComponent useCountComponent = Component
                    .text(" " + use + "/" + maxUse)
                    .color(color);
            name = name.append(useCountComponent);
        }
        var rawName = PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(name);
        var rawItemStack = getRaw();
        rawItemStack.set(DataComponents.CUSTOM_NAME, rawName);
    }

    /**
     * Sets the lore of this item stack based on its properties.
     * This includes setting the lore lines and usage.
     */
    public void setItemLore() {
        VOItem item = getOwner();
        setItemLore(item.getKey(), item.loreLineNum, item.usage);
    }

    /**
     * Sets the lore of this item stack based on the specified parameters.
     *
     * @param loreKey the key for the lore
     * @param lineNum the number of lore lines
     * @param usage   the usage of the item
     */
    public void setItemLore(Key loreKey, int lineNum, VOItemUsage usage) {
        List<net.minecraft.network.chat.Component> loreComponents = new ArrayList<>();
        for (int i = 0; i < lineNum; i++) {
            loreComponents.add(
                    PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(
                            Component.translatable(TranslationUtil.itemKey(loreKey, "lore" + (i + 1)))
                    )
            );
        }
        if (usage != VOItemUsage.NONE) {
            loreComponents.add(PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(Component.text(" ")));
            loreComponents.add(
                    PaperAdventure.WRAPPER_AWARE_SERIALIZER.serialize(
                            Component.translatable(TranslationUtil.guiKey("itemUsage"))
                                    .append(Component.translatable(TranslationUtil.guiKey("itemUsage", usage.getKey()))
                                    )
                    )
            );
        }
        var rawItemStack = getRaw();
        rawItemStack.set(DataComponents.LORE, new ItemLore(loreComponents));
    }

    /**
     * Sets the model of this item stack to the model of its owner.
     */
    public void setModel() {
        setModel(getOwner().getKey());
    }

    /**
     * Sets the model of this item stack to the specified model key.
     *
     * @param modelKey the key for the model
     */
    public void setModel(Key modelKey) {
        var rawItemStack = getRaw();
        rawItemStack.set(DataComponents.ITEM_MODEL, ResourceLocation.fromNamespaceAndPath(modelKey.namespace(), modelKey.key()));
    }

    /**
     * Sets the unique identifier for this item stack.
     */
    public void setUnique() {
        setProperty(VOCoreProperties.UNIQUE, UUID.randomUUID());
    }

    /**
     * Decreases the use count of this item stack.
     */
    public void decreaseUseCount() {
        if (!containsProperty(VOCoreProperties.USE)) return;
        int use = getProperty(VOCoreProperties.USE);
        if (use > 0) setProperty(VOCoreProperties.USE, use - 1);
        updateName();
    }

    /**
     * Gets the cooldown key for this item stack.
     * If the cooldown key is not set, it returns the owner key.
     *
     * @return the cooldown key
     */
    public Key getCoolDownKey() {
        if (containsProperty(VOCoreProperties.COOLTIME_KEY)) return getProperty(VOCoreProperties.COOLTIME_KEY);
        else return getProperty(VOCoreProperties.OWNER);
    }

    /**
     * Sets the cooldown key for this item stack.
     *
     * @param player the player using the item
     * @param itemStack the item stack being used
     */
    public void setCooltime(Player player, ItemStack itemStack) {
        if (!containsProperty(VOCoreProperties.COOLTIME)) return;
        VOPlayer voplayer = VoxelOdysseyCore.getVOPlayer(player);
        if(voplayer instanceof CoolDownHolder holder){
            CoolDown cooldown = holder.getCoolDown();
            cooldown.useItem(itemStack, getProperty(VOCoreProperties.COOLTIME));
        }
    }

    private net.minecraft.world.item.ItemStack getRaw() {
        return CraftItemStack.unwrap(handle);
    }

    @Override
    public boolean containsProperty(@NotNull Property<?> property) {
        var rawItemStack = getRaw();
        CompoundTag tag = rawItemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains(property.getKey().toString());
    }

    @Override
    public <U> U getProperty(@NotNull Property<U> property) {
        var rawItemStack = getRaw();
        CompoundTag tag = rawItemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains(property.getKey().toString())) {
            String jsonStr = tag.getString(property.getKey().toString());
            try {
                JsonElement element = JsonParser.parseString(jsonStr);
                return property.parseValue(element);
            } catch (DataFormatException e) {
                VoxelOdysseyCore.getLogger()
                                .log(
                                        Level.SEVERE,
                                        "ItemStack has invalid property " + property.getKey(),
                                        e
                                );
                return property.defaultValue();
            }
        }
        return property.defaultValue();
    }

    @Override
    public <U> VOItemStackWrapper setProperty(@NotNull Property<U> property, U value) {
        var rawItemStack = getRaw();
        CompoundTag tag = rawItemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putString(property.getKey().toString(), property.parseJson(value).toString());
        rawItemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return this;
    }
}
