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
package com.guy7cc.voxelodyssey.core.common;

import com.guy7cc.voxelodyssey.core.util.ReflectionUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

/**
 * Handles the lifecycle of a plugin by managing a list of {@link PluginLifecycleListener} instances.
 * It allows for the collection of listeners from fields and static fields, and executes lifecycle methods in order.
 */
public class GeneralPluginLifecycleHandler {
    private final JavaPlugin plugin;
    private List<PluginLifecycleListener> list = new ArrayList<>();

    public GeneralPluginLifecycleHandler(JavaPlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Registers a single {@link PluginLifecycleListener}.
     *
     * @param listener the listener to register
     */
    public void add(PluginLifecycleListener listener){
        list.add(listener);
    }

    /**
     * Registers a collection of {@link PluginLifecycleListener}s.
     *
     * @param listeners the collection of listeners to register
     */
    public void add(Collection<PluginLifecycleListener> listeners){
        listeners.forEach(this::add);
    }

    /**
     * Collects {@link PluginLifecycleListener}s from the fields of an object and registers them.
     *
     * @param obj the object to collect listeners from
     */
    public void collect(Object obj){
        List<Object> list = ReflectionUtil.collectFields(obj, o -> o instanceof PluginLifecycleListener, plugin.getLogger());
        add(list.stream().map(o -> (PluginLifecycleListener) o).toList());
    }

    /**
     * Collects static {@link PluginLifecycleListener}s from the fields of a class and registers them.
     *
     * @param clazz the class to collect listeners from
     */
    public void collectStatic(Class<?> clazz){
        List<Object> list = ReflectionUtil.collectStaticFields(clazz, o -> o instanceof PluginLifecycleListener, plugin.getLogger());
        add(list.stream().map(o -> (PluginLifecycleListener) o).toList());
    }

    /**
     * Executes a lifecycle method on all registered listeners in order.
     *
     * @param methodName the name of the lifecycle method to execute
     */
    public void executeLifecycleMethod(String methodName) {
        list.stream()
                .sorted(Comparator.comparingInt(listener -> getOrderValue(listener, methodName)))
                .forEach(listener -> invokeMethod(listener, methodName));
    }

    private int getOrderValue(PluginLifecycleListener listener, String methodName) {
        try {
            Method method = listener.getClass().getMethod(methodName, JavaPlugin.class);
            ExecutionOrder order = method.getAnnotation(ExecutionOrder.class);
            return order != null ? order.value().ordinal() : ExecutionOrder.Order.MIDDLE.ordinal();
        } catch (NoSuchMethodException e) {
            return ExecutionOrder.Order.MIDDLE.ordinal();
        }
    }

    private void invokeMethod(PluginLifecycleListener listener, String methodName) {
        try {
            Method method = listener.getClass().getMethod(methodName, JavaPlugin.class);
            method.invoke(listener, plugin);
        } catch (Exception e) {
            plugin.getLogger().log(
                    Level.SEVERE,
                    "Faild to invoke " + methodName,
                    e
            );
        }
    }

    /**
     * Executes the onPluginEnabled lifecycle method on all registered listeners.
     */
    public void onPluginEnabled() {
        executeLifecycleMethod("onPluginEnabled");
    }

    /**
     * Executes the onFirstTick lifecycle method on all registered listeners.
     */
    public void onFirstTick() {
        executeLifecycleMethod("onFirstTick");
    }

    /**
     * Executes the onPluginDisabled lifecycle method on all registered listeners.
     */
    public void onPluginDisabled() {
        executeLifecycleMethod("onPluginDisabled");
    }
}
