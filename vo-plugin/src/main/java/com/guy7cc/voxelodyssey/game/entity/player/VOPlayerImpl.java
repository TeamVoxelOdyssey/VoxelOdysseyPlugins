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
package com.guy7cc.voxelodyssey.game.entity.player;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.guy7cc.voxelodyssey.core.data.DataFormatException;
import com.guy7cc.voxelodyssey.core.entity.VOEntityType;
import com.guy7cc.voxelodyssey.core.entity.player.VOPlayer;
import com.guy7cc.voxelodyssey.core.gui.actionbar.ActionBarBridge;
import com.guy7cc.voxelodyssey.core.gui.actionbar.ActionBarBridgeImpl;
import com.guy7cc.voxelodyssey.core.gui.menu.MenuInventory;
import com.guy7cc.voxelodyssey.core.gui.menu.MenuInventoryProperty;
import com.guy7cc.voxelodyssey.core.gui.sidebar.SidebarBridge;
import com.guy7cc.voxelodyssey.core.gui.sidebar.SidebarBridgeImpl;
import com.guy7cc.voxelodyssey.core.gui.title.TitleBridge;
import com.guy7cc.voxelodyssey.core.gui.title.TitleBridgeImpl;
import com.guy7cc.voxelodyssey.core.item.CoolDown;
import com.guy7cc.voxelodyssey.core.item.CoolDownHolder;
import com.guy7cc.voxelodyssey.core.item.VOItem;
import com.guy7cc.voxelodyssey.core.item.VOItemStackWrapper;
import com.guy7cc.voxelodyssey.core.region.RegionContext;
import com.guy7cc.voxelodyssey.core.registry.Key;
import com.guy7cc.voxelodyssey.core.util.TranslationUtil;
import com.guy7cc.voxelodyssey.game.item.ModifierProvider;
import com.guy7cc.voxelodyssey.game.system.effect.VOEffectApplicable;
import com.guy7cc.voxelodyssey.game.system.effect.VOEffectRouter;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Implementation of the VOPlayer interface.
 * This replaces the NoOpPlayer implementation by registering the entity type to the registry.
 */
public class VOPlayerImpl implements VOPlayer, CoolDownHolder, VOEffectApplicable<Player> {
    private final Player handle;
    private double hp = 20;
    private double maxHp = 20;
    private double mp = 20;
    private double maxMp = 20;
    private int lv = 1;
    public final VOEffectRouter effectManager;
    public final CoolDown cooldown;
    private RegionContext regionContext;

    public final ActionBarBridge actionBar;
    public final SidebarBridge sidebar;
    public final TitleBridge titleBridge;
    private MenuInventory menu;

    protected VOPlayerImpl(Player player) {
        this.handle = player;
        this.effectManager = new VOEffectRouter(this);
        this.cooldown = new CoolDown();
        this.actionBar = new ActionBarBridgeImpl(
                player,
                false
        );
        this.sidebar = new SidebarBridgeImpl(
                player,
                TranslationUtil.gui("sidebar"),
                false);
        this.titleBridge = new TitleBridgeImpl(
                player,
                false
        );
        initialize();
    }

    @Override
    public Player getHandle() {
        return handle;
    }

    @Override
    public void tick(int globalTick) {
        // Set HP
        applyHpSilent();
        handle.setFoodLevel(16);

        effectManager.tick(globalTick);
        cooldown.tick(globalTick);
    }

    @Override
    public double getHp() {
        return hp;
    }

    @Override
    public void setHp(double hp) {
        this.hp = hp;
        applyHpSilent();
    }

    @Override
    public double getMaxHp() {
        return maxHp;
    }

    @Override
    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public CoolDown getCoolDown() {
        return cooldown;
    }

    @Override
    public VOEffectRouter getEffectRouter() {
        return effectManager;
    }

    private void refreshModifier() {
        effectManager.clearModifierAll();
        PlayerInventory inv = handle.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack == null || itemStack.isEmpty()) continue;
            VOItemStackWrapper state = VOItemStackWrapper.of(itemStack);
            if (!state.isVOItem()) return;
            VOItem voitem = state.getOwner();
            if(voitem instanceof ModifierProvider modifierItem){
                effectManager.addModifier(modifierItem.createModifier(state, handle, i));
            }
        }
    }

    public void openMenu(MenuInventoryProperty property) {
        MenuInventory newMenu = new MenuInventory(property);
        this.handle.openInventory(menu.getHandle());
        this.menu = newMenu;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        refreshModifier();
    }

    @EventHandler
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        hp = maxHp;
        refreshModifier();
    }

    @EventHandler
    public void onPlayerInventorySlotChange(PlayerInventorySlotChangeEvent event) {
        int slot = event.getSlot();
        effectManager.clearModifierFromInventory(slot);
        VOItemStackWrapper state = VOItemStackWrapper.of(event.getNewItemStack());
        if (!state.isVOItem()) return;
        VOItem voitem = state.getOwner();
        if(voitem instanceof ModifierProvider modifierItem){
            effectManager.addModifier(modifierItem.createModifier(state, handle, slot));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (menu != null) {
            if (event.getInventory() == menu.getHandle()) {
                menu.onInventoryClick(event);
            } else {
                menu = null;
            }
        }
    }

    @Override
    public VOPlayerImpl initialize() {
        effectManager.initialize();
        cooldown.initialize();
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        root.add("effect_manager", effectManager.toJson());
        root.add("cooltime", cooldown.toJson());
        return root;
    }

    @Override
    public VOPlayerImpl fromJson(JsonElement j) throws DataFormatException {
        try {
            JsonObject o = j.getAsJsonObject();
            effectManager.fromJson(o.getAsJsonObject("effect_manager"));
            cooldown.fromJson(o.getAsJsonObject("cooltime"));
            return this;
        } catch (Exception e) {
            throw new DataFormatException(getClass(), e);
        }
    }

    public static final class Type extends VOEntityType<VOPlayer, FactoryArgs> {
        public Type() {
            super(Key.vo("voplayer"));
        }

        @Override
        public VOPlayer create(FactoryArgs args) {
            throw new UnsupportedOperationException("Cannot spawn player");
        }

        @Override
        public VOPlayer wrap(Entity entity, FactoryArgs args) {
            if(!(entity instanceof Player player)) return null;
            return new VOPlayerImpl(player);
        }
    }
}
