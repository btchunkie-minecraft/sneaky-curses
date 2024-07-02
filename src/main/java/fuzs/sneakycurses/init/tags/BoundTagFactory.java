package fuzs.sneakycurses.init.tags;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

/**
 * A simple helper class for creating new {@link TagKey} instances via a pre-set
 * namespace.
 */
public final class BoundTagFactory {
    private static final Map<String, BoundTagFactory> VALUES = Maps.newConcurrentMap();
    /**
     * Factory instance for namespace <code>minecraft</code>.
     */
    public static final BoundTagFactory MINECRAFT = make("minecraft");
    /**
     * Factory instance for namespace <code>c</code>.
     */
    public static final BoundTagFactory COMMON = make("c");
    /**
     * Factory instance for namespace <code>fabric</code>.
     */
    public static final BoundTagFactory FABRIC = make("fabric");
    /**
     * Factory instance for namespace <code>forge</code>.
     */
    public static final BoundTagFactory FORGE = make("forge");
    /**
     * Factory instance for namespace <code>curios</code>.
     */
    public static final BoundTagFactory CURIOS = make("curios");
    /**
     * Factory instance for namespace <code>trinkets</code>.
     */
    public static final BoundTagFactory TRINKETS = make("trinkets");

    private final String namespace;

    private BoundTagFactory(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Construct a new factory instance backed by a provided namespace.
     *
     * @param namespace the namespace
     * @return new factory instance
     */
    public static BoundTagFactory make(String namespace) {
        return VALUES.computeIfAbsent(namespace, BoundTagFactory::new);
    }

    /**
     * Creates a new {@link TagKey} for any type of registry from a given path.
     *
     * @param registryKey key for registry to create key from
     * @param path        path for new tag key
     * @param <T>         registry type
     * @return new tag key
     */
    public <T> TagKey<T> registerTagKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return TagKey.create(registryKey, new ResourceLocation(this.namespace, path));
    }

    /**
     * Creates a new {@link TagKey} for blocks.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<Block> registerBlockTag(String path) {
        return this.registerTagKey(Registry.BLOCK_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for items.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<Item> registerItemTag(String path) {
        return this.registerTagKey(Registry.ITEM_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for fluids.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<Fluid> registerFluidTag(String path) {
        return this.registerTagKey(Registry.FLUID_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for entity types.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<EntityType<?>> registerEntityTypeTag(String path) {
        return this.registerTagKey(Registry.ENTITY_TYPE_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for enchantments.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<Enchantment> registerEnchantmentTag(String path) {
        return this.registerTagKey(Registry.ENCHANTMENT_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for biomes.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<Biome> registerBiomeTag(String path) {
        return this.registerTagKey(Registry.BIOME_REGISTRY, path);
    }

    /**
     * Creates a new {@link TagKey} for game events.
     *
     * @param path path for new tag key
     * @return new tag key
     */
    public TagKey<GameEvent> registerGameEventTag(String path) {
        return this.registerTagKey(Registry.GAME_EVENT_REGISTRY, path);
    }

    // Remove this method as DamageType does not exist in 1.18
    // /**
    // * Creates a new {@link TagKey} for damage types.
    // *
    // * @param path path for new tag key
    // * @return new tag key
    // */
    // public TagKey<DamageType> registerDamageTypeTag(String path) {
    // return this.registerTagKey(Registries.DAMAGE_TYPE, path);
    // }
}
