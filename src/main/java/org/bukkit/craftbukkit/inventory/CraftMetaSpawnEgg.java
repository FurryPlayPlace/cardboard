package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftLegacy;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaSpawnEgg extends CraftMetaItem implements SpawnEggMeta {

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKey ENTITY_ID = new ItemMetaKey("id");

    private EntityType spawnedType;
    private NbtCompound entityTag;

    public CraftMetaSpawnEgg(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaSpawnEgg)) return;

        CraftMetaSpawnEgg egg = (CraftMetaSpawnEgg) meta;
        this.spawnedType = egg.spawnedType;

        updateMaterial(null); // Trigger type population
    }

    public CraftMetaSpawnEgg(NbtCompound tag) {
        super(tag);

        if (tag.contains(ENTITY_TAG.NBT)) entityTag = tag.getCompound(ENTITY_TAG.NBT);
    }

    public CraftMetaSpawnEgg(Map<String, Object> map) {
        super(map);

        String entityType = SerializableMeta.getString(map, ENTITY_ID.BUKKIT, true);
        if (entityType != null) this.spawnedType = EntityType.fromName(entityType);
    }

    @Override
    public void deserializeInternal(NbtCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(ENTITY_TAG.NBT)) {
            entityTag = tag.getCompound(ENTITY_TAG.NBT);

            if (context instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) context;

                // Duplicated from constructor
                String entityType = SerializableMeta.getString(map, ENTITY_ID.BUKKIT, true);
                if (entityType != null) this.spawnedType = EntityType.fromName(entityType);
            }

            if (this.spawnedType != null) entityTag.remove(ENTITY_ID.NBT); // We have a valid spawn type, just remove the ID now

            // See if we can read a converted ID tag
            if (entityTag.contains(ENTITY_ID.NBT)) this.spawnedType = EntityType.fromName(new Identifier(entityTag.getString(ENTITY_ID.NBT)).getPath());
        }
    }

    @Override
    void serializeInternal(Map<String, NbtElement> internalTags) {
        if (entityTag != null && !entityTag.isEmpty()) internalTags.put(ENTITY_TAG.NBT, entityTag);
    }

    @Override
    void applyToItem(NbtCompound tag) {
        super.applyToItem(tag);

        if (!isSpawnEggEmpty() && entityTag == null) entityTag = new NbtCompound();
        if (entityTag != null) tag.put(ENTITY_TAG.NBT, entityTag);
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case BAT_SPAWN_EGG:
            case BEE_SPAWN_EGG:
            case BLAZE_SPAWN_EGG:
            case CAT_SPAWN_EGG:
            case CAVE_SPIDER_SPAWN_EGG:
            case CHICKEN_SPAWN_EGG:
            case COD_SPAWN_EGG:
            case COW_SPAWN_EGG:
            case CREEPER_SPAWN_EGG:
            case DOLPHIN_SPAWN_EGG:
            case DONKEY_SPAWN_EGG:
            case DROWNED_SPAWN_EGG:
            case ELDER_GUARDIAN_SPAWN_EGG:
            case ENDERMAN_SPAWN_EGG:
            case ENDERMITE_SPAWN_EGG:
            case EVOKER_SPAWN_EGG:
            case FOX_SPAWN_EGG:
            case GHAST_SPAWN_EGG:
            case GUARDIAN_SPAWN_EGG:
            case HORSE_SPAWN_EGG:
            case HUSK_SPAWN_EGG:
            case LLAMA_SPAWN_EGG:
            case MAGMA_CUBE_SPAWN_EGG:
            case MOOSHROOM_SPAWN_EGG:
            case MULE_SPAWN_EGG:
            case OCELOT_SPAWN_EGG:
            case PANDA_SPAWN_EGG:
            case PARROT_SPAWN_EGG:
            case PHANTOM_SPAWN_EGG:
            case PIG_SPAWN_EGG:
            case PILLAGER_SPAWN_EGG:
            case POLAR_BEAR_SPAWN_EGG:
            case PUFFERFISH_SPAWN_EGG:
            case RABBIT_SPAWN_EGG:
            case RAVAGER_SPAWN_EGG:
            case SALMON_SPAWN_EGG:
            case SHEEP_SPAWN_EGG:
            case SHULKER_SPAWN_EGG:
            case SILVERFISH_SPAWN_EGG:
            case SKELETON_HORSE_SPAWN_EGG:
            case SKELETON_SPAWN_EGG:
            case SLIME_SPAWN_EGG:
            case SPIDER_SPAWN_EGG:
            case SQUID_SPAWN_EGG:
            case STRAY_SPAWN_EGG:
            case TRADER_LLAMA_SPAWN_EGG:
            case TROPICAL_FISH_SPAWN_EGG:
            case TURTLE_SPAWN_EGG:
            case VEX_SPAWN_EGG:
            case VILLAGER_SPAWN_EGG:
            case VINDICATOR_SPAWN_EGG:
            case WANDERING_TRADER_SPAWN_EGG:
            case WITCH_SPAWN_EGG:
            case WITHER_SKELETON_SPAWN_EGG:
            case WOLF_SPAWN_EGG:
            case ZOMBIE_HORSE_SPAWN_EGG:
           // case ZOMBIE_PIGMAN_SPAWN_EGG:
            case ZOMBIE_SPAWN_EGG:
            case ZOMBIE_VILLAGER_SPAWN_EGG:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSpawnEggEmpty();
    }

    boolean isSpawnEggEmpty() {
        return !(hasSpawnedType() || entityTag != null);
    }

    boolean hasSpawnedType() {
        return spawnedType != null;
    }

    @Override
    public EntityType getSpawnedType() {
        throw new UnsupportedOperationException("Must check item type to get spawned type");
    }

    @Override
    public void setSpawnedType(EntityType type) {
        throw new UnsupportedOperationException("Must change item type to set spawned type");
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) return false;

        if (meta instanceof CraftMetaSpawnEgg) {
            CraftMetaSpawnEgg that = (CraftMetaSpawnEgg) meta;
            return hasSpawnedType() ? that.hasSpawnedType() && this.spawnedType.equals(that.spawnedType) : !that.hasSpawnedType()
                    && entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : entityTag == null;
        }
        return true;
    }

    @Override
    public boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSpawnEgg || isSpawnEggEmpty());
    }

    @Override
    public int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasSpawnedType())  hash = 73 * hash + spawnedType.hashCode();
        if (entityTag != null) hash = 73 * hash + entityTag.hashCode();

        return original != hash ? CraftMetaSpawnEgg.class.hashCode() ^ hash : hash;
    }

    @Override
    public Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }

    @Override
    public CraftMetaSpawnEgg clone() {
        CraftMetaSpawnEgg clone = (CraftMetaSpawnEgg) super.clone();
        clone.spawnedType = spawnedType;
        if (entityTag != null) clone.entityTag = entityTag.copy();
        return clone;
    }

    @Override
    public Material updateMaterial(Material material) {
        if (spawnedType == null) {
            spawnedType = EntityType.fromId(getDamage());
            setDamage(0);
        }

        if (spawnedType != null) {
            if (entityTag != null) entityTag.remove(ENTITY_ID.NBT); // Remove ID tag as it is now in the material
            return CraftLegacy.fromLegacy(new MaterialData(Material.LEGACY_MONSTER_EGG, (byte) spawnedType.getTypeId()));
        }
        return super.updateMaterial(material);
    }

    @Override
    public org.bukkit.entity.EntityType getCustomSpawnedType() {
        return Optional.ofNullable(this.entityTag).map(tag -> tag.getString(CraftMetaSpawnEgg.ENTITY_ID.NBT)).flatMap(net.minecraft.entity.EntityType::get).map(CraftMagicNumbers::getEntityType).orElse(null);
    }

    @Override
    public void setCustomSpawnedType(org.bukkit.entity.EntityType type) {
        if (type == null) {
            if (this.entityTag != null) {
                this.entityTag.remove(CraftMetaSpawnEgg.ENTITY_ID.NBT);
            }
        } else {
            if (this.entityTag == null) {
                this.entityTag = new NbtCompound();
            }
            this.entityTag.putString(CraftMetaSpawnEgg.ENTITY_ID.NBT, type.key().toString());
        }
    }

}