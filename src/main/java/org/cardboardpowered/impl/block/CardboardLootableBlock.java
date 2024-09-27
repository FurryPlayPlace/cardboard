package org.cardboardpowered.impl.block;

import net.kyori.adventure.text.Component;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftContainer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.destroystokyo.paper.loottable.PaperLootableBlockInventory;
import com.destroystokyo.paper.loottable.PaperLootableInventory;


public abstract class CardboardLootableBlock<T extends LootableContainerBlockEntity> extends CraftContainer<T>
implements Nameable, Lootable, PaperLootableBlockInventory {

    public CardboardLootableBlock(Block block, Class<T> tileEntityClass) {
        super(block, tileEntityClass);
    }

    public CardboardLootableBlock(Material material, T tileEntity) {
        super(material, tileEntity);
    }

    @Override
    public void applyTo(T lootable) {
        super.applyTo(lootable);
        if (this.getSnapshot().lootTableId == null) lootable.setLootTable((Identifier) null, 0L);
    }

    @Override
    public LootTable getLootTable() {
        return (getSnapshot().lootTableId == null) ? null : Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(getSnapshot().lootTableId));
    }

    @Override
    public void setLootTable(LootTable table) {
        setLootTable(table, getSeed());
    }

    @Override
    public long getSeed() {
        return getSnapshot().lootTableSeed;
    }

    @Override
    public void setSeed(long seed) {
        setLootTable(getLootTable(), seed);
    }

    public void setLootTable(LootTable table, long seed) {
//<<<<<<< HEAD
        getSnapshot().setLootTable(((table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey())), seed);
/*
        =======
    	Registry<net.minecraft.loot.LootTable> reg = CraftServer.server.getRegistryManager().get(RegistryKeys.LOOT_TABLE);
    	Optional<net.minecraft.loot.LootTable> mc_table = reg.getOrEmpty( CraftNamespacedKey.toMinecraft(table.getKey()) );
    	
    	if (mc_table.isPresent()) {
    		Optional<RegistryKey<net.minecraft.loot.LootTable>> mc_key = reg.getKey(mc_table.get());
    		getSnapshot().setLootTable(mc_key.get(), seed);
    	} else {
    		getSnapshot().setLootTable(null, seed);
    	}
    	
        // getSnapshot().setLootTable(((table == null) ? null : CraftNamespacedKey.toMinecraft(table.getKey())), seed);
>>>>>>> ccc00fa (Update Paper-API to 1.20.1)
*/
    }


}