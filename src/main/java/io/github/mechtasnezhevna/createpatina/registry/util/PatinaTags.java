package io.github.mechtasnezhevna.createpatina.registry.util;

import io.github.mechtasnezhevna.createpatina.CreatePatina;
import io.github.mechtasnezhevna.createpatina.util.WeatheringType;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

import static io.github.mechtasnezhevna.createpatina.registry.util.PatinaTags.NameSpace.MOD;

public final class PatinaTags {

    public static Map<WeatheringType, PatinaItemTags> BY_TYPE = new EnumMap<>(WeatheringType.class);

    static {
        BY_TYPE.put(WeatheringType.UNAFFECTED, PatinaItemTags.UNAFFECTED);
        BY_TYPE.put(WeatheringType.EXPOSED, PatinaItemTags.EXPOSED);
        BY_TYPE.put(WeatheringType.WEATHERED, PatinaItemTags.WEATHERED);
        BY_TYPE.put(WeatheringType.OXIDIZED, PatinaItemTags.OXIDIZED);
        BY_TYPE.put(WeatheringType.WAXED, PatinaItemTags.WAXED);
        BY_TYPE.put(WeatheringType.WAXED_EXPOSED, PatinaItemTags.WAXED_EXPOSED);
        BY_TYPE.put(WeatheringType.WAXED_WEATHERED, PatinaItemTags.WAXED_WEATHERED);
        BY_TYPE.put(WeatheringType.WAXED_OXIDIZED, PatinaItemTags.WAXED_OXIDIZED);
    }

    public enum NameSpace {
        MOD(CreatePatina.MODID);

        public final String id;

        NameSpace(String id) {
            this.id = id;
        }

        public ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(this.id, path);
        }

        public ResourceLocation id(Enum<?> entry, @Nullable String pathOverride) {
            return this.id(pathOverride != null ? pathOverride : Lang.asId(entry.name()));
        }
    }

    public enum PatinaBlockTags {

        FAN_PROCESSING_CATALYSTS_HONEYING(MOD, "fan_processing_catalysts/honeying");

        public final TagKey<Block> tag;

        PatinaBlockTags() {
            this(MOD);
        }

        PatinaBlockTags(NameSpace namespace) {
            this(namespace, null);
        }

        PatinaBlockTags(NameSpace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.BLOCK, namespace.id(this, pathOverride));
        }

        public boolean matches(BlockState state) {
            return state.is(tag);
        }
    }

    public enum PatinaItemTags {
        UNAFFECTED,
        EXPOSED,
        WEATHERED,
        OXIDIZED,
        WAXED,
        WAXED_EXPOSED,
        WAXED_WEATHERED,
        WAXED_OXIDIZED
        ;

        public final TagKey<Item> tag;

        PatinaItemTags() {
            this(NameSpace.MOD);
        }

        PatinaItemTags(NameSpace namespace) {
            this(namespace, null);
        }

        PatinaItemTags(NameSpace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.ITEM, namespace.id(this, pathOverride));
        }

        @SuppressWarnings("deprecation")
        public boolean matches(Item item) {
            return item.builtInRegistryHolder()
                    .is(tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(tag);
        }
    }

    public enum PatinaFluidTags {

        FAN_PROCESSING_CATALYSTS_HONEYING(MOD, "fan_processing_catalysts/honeying");

        public final TagKey<Fluid> tag;

        PatinaFluidTags() {
            this(MOD);
        }

        PatinaFluidTags(NameSpace namespace) {
            this(namespace, null);
        }

        PatinaFluidTags(NameSpace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.FLUID, namespace.id(this, pathOverride));
        }

        public boolean matches(FluidState state) {
            return state.is(tag);
        }
    }
}
