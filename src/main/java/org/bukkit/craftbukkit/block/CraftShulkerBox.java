package org.bukkit.craftbukkit.block;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.Inventory;
import org.cardboardpowered.impl.block.CardboardLootableBlock;

public class CraftShulkerBox extends CardboardLootableBlock<ShulkerBoxBlockEntity> implements ShulkerBox {

    public CraftShulkerBox(final Block block) {
        super(block, ShulkerBoxBlockEntity.class);
    }

    public CraftShulkerBox(final Material material, final ShulkerBoxBlockEntity te) {
        super(material, te);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced())
            return this.getSnapshotInventory();

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public DyeColor getColor() {
        net.minecraft.block.Block block = CraftMagicNumbers.getBlock(this.getType());

        return DyeColor.getByWoolData((byte) ((ShulkerBoxBlock) block).getColor().getId());
    }

    @Override
    public void open() {
        // TODO Auto-generated method stub
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

}