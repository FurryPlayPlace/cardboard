/**
 * The Bukkit for Fabric Project
 * Copyright (C) 2020 Javazilla Software and contributors
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.javazilla.bukkitfabric.interfaces;

import java.util.Map;

import org.bukkit.Chunk;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registry;
import net.minecraft.world.biome.Biome;

public interface IMixinChunk {

    Chunk getBukkitChunk();

    // Lnet/minecraft/world/chunk/Chunk;blockEntities:Ljava/util/Map;
    //     public final Map<BlockPos, BlockEntity> blockEntities = Maps.newHashMap();

    Map<BlockPos, BlockEntity> cardboard_getBlockEntities();

    default Registry<Biome> bridge$biomeRegistry() {
        return null;
    }
    
}