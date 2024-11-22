package org.cardboardpowered.impl;

import net.kyori.adventure.text.Component;
import net.minecraft.enchantment.BindingCurseEnchantment;
import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Set;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.enchantments.EnchantmentRarity;

public class CardboardEnchantment extends Enchantment implements Handleable<net.minecraft.enchantment.Enchantment> {

    private final net.minecraft.enchantment.Enchantment target;

    private final NamespacedKey key;
    private final net.minecraft.enchantment.Enchantment handle;
    private final int id;
    
    public CardboardEnchantment(NamespacedKey key, net.minecraft.enchantment.Enchantment handle) {
    	super(CraftNamespacedKey.fromMinecraft(Registries.ENCHANTMENT.getId(handle)));
        this.key = key;
        this.handle = handle;
        this.id = Registries.ENCHANTMENT.getRawId(handle);
        this.target = handle;
    }
    
    @Deprecated
    public CardboardEnchantment(net.minecraft.enchantment.Enchantment target) {
    	this(CraftNamespacedKey.fromMinecraft(Registries.ENCHANTMENT.getId(target)), target);
    }

    @Override
    public int getMaxLevel() {
        return target.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return target.getMinLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        switch (target.target) {
            case ARMOR:
                return EnchantmentTarget.ARMOR;
            case ARMOR_FEET:
                return EnchantmentTarget.ARMOR_FEET;
            case ARMOR_HEAD:
                return EnchantmentTarget.ARMOR_HEAD;
            case ARMOR_LEGS:
                return EnchantmentTarget.ARMOR_LEGS;
            case ARMOR_CHEST:
                return EnchantmentTarget.ARMOR_TORSO;
            case DIGGER:
                return EnchantmentTarget.TOOL;
            case WEAPON:
                return EnchantmentTarget.WEAPON;
            case BOW:
                return EnchantmentTarget.BOW;
            case FISHING_ROD:
                return EnchantmentTarget.FISHING_ROD;
            case BREAKABLE:
                return EnchantmentTarget.BREAKABLE;
            case WEARABLE:
                return EnchantmentTarget.WEARABLE;
            case TRIDENT:
                return EnchantmentTarget.TRIDENT;
            case CROSSBOW:
                return EnchantmentTarget.CROSSBOW;
            default:
                return null;
        }
    }

    @Override
    public boolean isTreasure() {
        return target.isTreasure();
    }

    @Override
    public boolean isCursed() {
        return target instanceof BindingCurseEnchantment || target instanceof VanishingCurseEnchantment;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return target.isAcceptableItem(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        switch (Registries.ENCHANTMENT.getRawId(target)) {
            case 0:
                return "PROTECTION_ENVIRONMENTAL";
            case 1:
                return "PROTECTION_FIRE";
            case 2:
                return "PROTECTION_FALL";
            case 3:
                return "PROTECTION_EXPLOSIONS";
            case 4:
                return "PROTECTION_PROJECTILE";
            case 5:
                return "OXYGEN";
            case 6:
                return "WATER_WORKER";
            case 7:
                return "THORNS";
            case 8:
                return "DEPTH_STRIDER";
            case 9:
                return "FROST_WALKER";
            case 10:
                return "BINDING_CURSE";
            case 11:
                return "DAMAGE_ALL";
            case 12:
                return "DAMAGE_UNDEAD";
            case 13:
                return "DAMAGE_ARTHROPODS";
            case 14:
                return "KNOCKBACK";
            case 15:
                return "FIRE_ASPECT";
            case 16:
                return "LOOT_BONUS_MOBS";
            case 17:
                return "SWEEPING_EDGE";
            case 18:
                return "DIG_SPEED";
            case 19:
                return "SILK_TOUCH";
            case 20:
                return "DURABILITY";
            case 21:
                return "LOOT_BONUS_BLOCKS";
            case 22:
                return "ARROW_DAMAGE";
            case 23:
                return "ARROW_KNOCKBACK";
            case 24:
                return "ARROW_FIRE";
            case 25:
                return "ARROW_INFINITE";
            case 26:
                return "LUCK";
            case 27:
                return "LURE";
            case 28:
                return "LOYALTY";
            case 29:
                return "IMPALING";
            case 30:
                return "RIPTIDE";
            case 31:
                return "CHANNELING";
            case 32:
                return "MULTISHOT";
            case 33:
                return "QUICK_CHARGE";
            case 34:
                return "PIERCING";
            case 35:
                return "MENDING";
            case 36:
                return "VANISHING_CURSE";
            default:
                return "UNKNOWN_ENCHANT_" + Registries.ENCHANTMENT.getId(target);
        }
    }

    public static net.minecraft.enchantment.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) enchantment = ((EnchantmentWrapper) enchantment).getEnchantment();
        if (enchantment instanceof CardboardEnchantment) return ((CardboardEnchantment) enchantment).target;

        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) other = ((EnchantmentWrapper) other).getEnchantment();
        if (!(other instanceof CardboardEnchantment)) return false;

        CardboardEnchantment ench = (CardboardEnchantment) other;
        return !target.canCombine(ench.target);
    }

    public net.minecraft.enchantment.Enchantment getHandle() {
        return target;
    }

    @Override
    public @NotNull Component displayName(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Set<EquipmentSlot> getActiveSlots() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getDamageIncrease(int arg0, @NotNull EntityCategory arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public @NotNull EnchantmentRarity getRarity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isDiscoverable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isTradeable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public @NotNull String translationKey() {
        // TODO Auto-generated method stub
        return null;
    }

	public static Enchantment minecraftHolderToBukkit(
			RegistryEntry<net.minecraft.enchantment.Enchantment> id) {
		// TODO Auto-generated method stub
		return CardboardEnchantment.minecraftToBukkit(id.value());
	}

	public static Enchantment minecraftToBukkit(net.minecraft.enchantment.Enchantment minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, RegistryKeys.ENCHANTMENT, Registry.ENCHANTMENT);
    }

}