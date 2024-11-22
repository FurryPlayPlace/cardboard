package org.bukkit.craftbukkit;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.bukkit.GameEvent;
import org.bukkit.Keyed;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.generator.structure.CraftStructure;
import org.bukkit.craftbukkit.generator.structure.CraftStructureType;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimPattern;
// import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.potion.PotionEffectType;
import org.cardboardpowered.impl.CardboardEnchantment;
import org.cardboardpowered.impl.CardboardPotionEffectType;
import org.cardboardpowered.impl.entity.WolfImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// <<<<<<< HEAD
import net.minecraft.registry.DynamicRegistryManager;
// =======
import net.minecraft.block.entity.BannerPattern;
//import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
// >>>>>>> ccc00fa (Update Paper-API to 1.20.1)

// TODO
public class CraftRegistry<B extends Keyed, M> implements Registry<B> {

	private static DynamicRegistryManager registry;
	private final Class<? super B> bukkitClass;
    private final net.minecraft.registry.Registry<M> minecraftRegistry;
    private final BiFunction<NamespacedKey, M, B> minecraftToBukkit;
    
    private final Map<NamespacedKey, B> cache = new HashMap<NamespacedKey, B>();
    private final Map<B, NamespacedKey> byValue = new IdentityHashMap<B, NamespacedKey>();
    
    private boolean init;
    
    public CraftRegistry(Class<? super B> bukkitClass, net.minecraft.registry.Registry<M> minecraftRegistry, BiFunction<NamespacedKey, M, B> minecraftToBukkit) {
        this.bukkitClass = bukkitClass;
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
    }
    
    public static <B extends Keyed, M> B minecraftToBukkit(M minecraft, RegistryKey<net.minecraft.registry.Registry<M>> registryKey, Registry<B> bukkitRegistry) {
        // Preconditions.checkArgument((minecraft != null ? 1 : 0) != 0);
        net.minecraft.registry.Registry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);
        Keyed bukkit = bukkitRegistry.get(
        		CraftNamespacedKey.fromMinecraft(
        				registry.getKey(minecraft).orElseThrow(
        						() -> new IllegalStateException(
        								String.format("Cannot convert '%s' to bukkit representation, since it is not registered.", minecraft)
        						)
        				).getValue()
        		)
        	);
        // Preconditions.checkArgument((bukkit != null ? 1 : 0) != 0);
        return (B)bukkit;
    }
    
    public static void setMinecraftRegistry(DynamicRegistryManager registry) {
        CraftRegistry.registry = registry;
    }
    
	@Override
	public Iterator<B> iterator() {
		 return this.stream().iterator();
	}
	
    public B createBukkit(NamespacedKey namespacedKey, M minecraft) {
        if (minecraft == null) {
            return null;
        }

        return this.minecraftToBukkit.apply(namespacedKey, minecraft);
    }

	@Override
	public @Nullable B get(@NotNull NamespacedKey namespacedKey) {
		 B cached = this.cache.get(namespacedKey);
	        if (cached != null) {
	            return cached;
	        }

	        // Make sure that the bukkit class is loaded before creating an instance.
	        // This ensures that only one instance with a given key is created.
	        //
	        // Without this code (when bukkit class is not loaded):
	        // Registry#get -> #createBukkit -> (load class -> create default) -> put in cache
	        // Result: Registry#get != <bukkitClass>.<field> for possible one registry item
	        //
	        // With this code (when bukkit class is not loaded):
	        // Registry#get -> (load class -> create default) -> Registry#get -> get from cache
	        // Result: Registry#get == <bukkitClass>.<field>
	        if (!this.init) {
	            this.init = true;
	            try {
	                Class.forName(this.bukkitClass.getName());
	            } catch (ClassNotFoundException e) {
	                throw new RuntimeException("Could not load registry class " + this.bukkitClass, e);
	            }

	            return this.get(namespacedKey);
	        }

	        B bukkit = this.createBukkit(namespacedKey, this.minecraftRegistry.getOrEmpty(CraftNamespacedKey.toMinecraft(namespacedKey)).orElse(null));
	        if (bukkit == null) {
	            return null;
	        }

	        this.cache.put(namespacedKey, bukkit);
	        this.byValue.put(bukkit, namespacedKey); // Paper - improve Registry

	        return bukkit;
	}

	public static <B extends Keyed> Registry<?> createRegistry(Class<B> bukkitClass, DynamicRegistryManager registryHolder) {
		// TODO Auto-generated method stub
		if (bukkitClass == Enchantment.class) {
            return new CraftRegistry<CardboardEnchantment, net.minecraft.enchantment.Enchantment>(Enchantment.class, registryHolder.get(RegistryKeys.ENCHANTMENT), CardboardEnchantment::new);
        }
        if (bukkitClass == GameEvent.class) {
            return new CraftRegistry<>(GameEvent.class, registryHolder.get(RegistryKeys.GAME_EVENT), CraftGameEvent::new);
        }
		if (bukkitClass == MusicInstrument.class) {
			return new CraftRegistry<>(MusicInstrument.class, registryHolder.get(RegistryKeys.INSTRUMENT), CraftMusicInstrument::new);
		}

		if (bukkitClass == PotionEffectType.class) {
            return new CraftRegistry<>(PotionEffectType.class, registryHolder.get(RegistryKeys.STATUS_EFFECT), CardboardPotionEffectType::new);
        }
        
        if (bukkitClass == org.bukkit.generator.structure.Structure.class) {
            return new CraftRegistry<>(org.bukkit.generator.structure.Structure.class, registryHolder.get(RegistryKeys.STRUCTURE), CraftStructure::new);
        }
        if (bukkitClass == StructureType.class) {
            return new CraftRegistry<>(StructureType.class, Registries.STRUCTURE_TYPE, CraftStructureType::new);
        }
        if (bukkitClass == TrimMaterial.class) {
            return new CraftRegistry<>(TrimMaterial.class, registryHolder.get(RegistryKeys.TRIM_MATERIAL), CraftTrimMaterial::new);
        }
        
        /*
        if (bukkitClass == TrimPattern.class) {
            return new CraftRegistry<>(TrimPattern.class, registryHolder.get(RegistryKeys.TRIM_PATTERN), CraftTrimPattern::new);
        }
        if (bukkitClass == DamageType.class) {
            return new CraftRegistry<>(DamageType.class, registryHolder.get(RegistryKeys.DAMAGE_TYPE), CraftDamageType::new);
        }
        if (bukkitClass == Wolf.Variant.class) {
            return new CraftRegistry<>(Wolf.Variant.class, registryHolder.get(RegistryKeys.WOLF_VARIANT), WolfImpl.CraftVariant::new);
        }
        // */
		
		return null;
	}

// <<<<<<< HEAD
// =======

    public static <B extends Keyed, M> RegistryEntry<M> bukkitToMinecraftHolder(B bukkit, RegistryKey<net.minecraft.registry.Registry<M>> registryKey) {
        // Preconditions.checkArgument(bukkit != null);

        net.minecraft.registry.Registry<M> registry = CraftRegistry.getMinecraftRegistry(registryKey);

        // TODO (M)
        if (registry.getEntry((M) CraftRegistry.bukkitToMinecraft(bukkit)) instanceof RegistryEntry.Reference<M> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit
                + ", this can happen if a plugin creates its own registry entry with out properly registering it.");
    }

	public static DynamicRegistryManager getMinecraftRegistry() {
		// TODO Auto-generated method stub
		return CraftServer.server.getRegistryManager();
	}

    public static <E> net.minecraft.registry.Registry<E> getMinecraftRegistry(RegistryKey<net.minecraft.registry.Registry<E>> key) {
        return CraftServer.server.getRegistryManager().get(key);
    }
   

	@Override
	public @NotNull Stream<B> stream() {
		return this.minecraftRegistry.getIds().stream().map(minecraftKey -> this.get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
	}

    /**
     * Usage note: Only use this method to delegate the conversion methods from the individual Craft classes to here.
     * Do not use it in other parts of CraftBukkit, use the methods in the respective Craft classes instead.
     *
     * @param bukkit the bukkit representation
     * @return the minecraft representation of the bukkit value
     */
    public static <B extends Keyed, M> M bukkitToMinecraft(B bukkit) {
        // Preconditions.checkArgument(bukkit != null);
        return ((Handleable<M>) bukkit).getHandle();
    }
    
    

// >>>>>>> ccc00fa (Update Paper-API to 1.20.1)
    /*private static DynamicRegistryManager registry;

    public static void setMinecraftRegistry(DynamicRegistryManager registry) {
        Preconditions.checkState(CraftRegistry.registry == null, "Registry already set");
        CraftRegistry.registry = registry;
    }

    public static DynamicRegistryManager getMinecraftRegistry() {
        return registry;
    }

    public static <E> net.minecraft.util.registry.Registry<E> getMinecraftRegistry(RegistryKey<net.minecraft.util.registry.Registry<E>> key) {
        return getMinecraftRegistry().get(key);
    }

    public static <B extends Keyed> Registry<?> createRegistry(Class<B> bukkitClass, DynamicRegistryManager registryHolder) {
        if (bukkitClass == GameEvent.class) {
            return new CraftRegistry<>(registryHolder.get(RegistryKeys.GAME_EVENT), CraftGameEvent::new);
        }
        if (bukkitClass == MusicInstrument.class) {
            return new CraftRegistry<>(registryHolder.get(RegistryKeys.INSTRUMENT), CraftMusicInstrument::new);
        }
        if (bukkitClass == Structure.class) {
            return new CraftRegistry<>(registryHolder.get(RegistryKeys.STRUCTURE), CraftStructure::new);
        }
        if (bukkitClass == StructureType.class) {
            return new CraftRegistry<>(Registries.STRUCTURE_TYPE, CraftStructureType::new);
        }
        if (bukkitClass == TrimMaterial.class) {
            return new CraftRegistry<>(registryHolder.get(RegistryKeys.TRIM_MATERIAL), CraftTrimMaterial::new);
        }
        if (bukkitClass == TrimPattern.class) {
            return new CraftRegistry<>(registryHolder.get(RegistryKeys.TRIM_PATTERN), CraftTrimPattern::new);
        }

        return null;
    }

    private final Map<NamespacedKey, B> cache = new HashMap<>();
    private final net.minecraft.registry.Registry<M> minecraftRegistry;
    private final BiFunction<NamespacedKey, M, B> minecraftToBukkit;

    public CraftRegistry(net.minecraft.util.registry.Registry<M> minecraftRegistry, BiFunction<NamespacedKey, M, B> minecraftToBukkit) {
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
    }

    @Override
    public B get(NamespacedKey namespacedKey) {
        B cached = cache.get(namespacedKey);
        if (cached != null) {
            return cached;
        }

        B bukkit = createBukkit(namespacedKey, minecraftRegistry.getOrEmpty(CraftNamespacedKey.toMinecraft(namespacedKey)).orElse(null));
        if (bukkit == null) {
            return null;
        }

        cache.put(namespacedKey, bukkit);

        return bukkit;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return minecraftRegistry.getIds().stream().map(minecraftKey -> get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }

    @Override
    public Iterator<B> iterator() {
        return stream().iterator();
    }

    public B createBukkit(NamespacedKey namespacedKey, M minecraft) {
        if (minecraft == null) {
            return null;
        }

        return minecraftToBukkit.apply(namespacedKey, minecraft);
    }

    public Stream<B> values() {
        return minecraftRegistry.getIds().stream().map(minecraftKey -> get(CraftNamespacedKey.fromMinecraft(minecraftKey)));
    }*/
}
