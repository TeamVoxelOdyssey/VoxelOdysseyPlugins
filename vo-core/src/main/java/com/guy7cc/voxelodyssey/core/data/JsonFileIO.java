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
package com.guy7cc.voxelodyssey.core.data;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class for reading and writing JSON files.
 * It uses Gson for JSON parsing and serialization.
 */
public class JsonFileIO {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UuidAdapter())
            .setPrettyPrinting()
            .create();
    private final Logger logger;

    public JsonFileIO(Logger logger){
        this.logger = logger;
    }

    /**
     * Saves a JsonElement to a file.
     *
     * @param element the JsonElement to save
     * @param file the file to save to
     */
    public void save(@NotNull JsonElement element, @NotNull File file) {
        try {
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(gson.toJson(element));
            writer.close();
        } catch (IOException exception) {
            logger.log(
                    Level.SEVERE,
                    String.format("Could not save %s due to I/O errors. ", file),
                    exception
            );
        } catch (SecurityException exception) {
            logger.log(
                    Level.SEVERE,
                    String.format("Could not save %s due to security problems. ", file),
                    exception
            );
        }
    }

    /**
     * Loads a JsonElement from a file.
     *
     * @param file the file to load from
     * @return the loaded JsonElement, or null if the file could not be read
     */
    @Nullable
    public JsonElement load(@NotNull File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return JsonParser.parseString(sb.toString());
        } catch (FileNotFoundException exception) {
            return null;
        } catch (IOException exception) {
            logger.log(
                    Level.SEVERE,
                    String.format("Could not read %s due to I/O errors.", file),
                    exception
            );
        } catch (JsonParseException exception) {
            logger.log(
                    Level.SEVERE,
                    String.format("Could not read %s because the file format was invalid.", file),
                    exception
            );
        } catch (ClassCastException exception) {
            logger.log(
                    Level.SEVERE,
                    String.format("Could not read %s because the element was not JsonObject", file),
                    exception
            );
        }
        return null;
    }
}
