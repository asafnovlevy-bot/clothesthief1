package dev.sofi.clothesthief;

import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

import dev.sofi.clothesthief.item.ClothesItem;

public final class ModContent {
	/** true = this player/villager has lost its natural clothes. Villagers never get them back. */
	public static final AttachmentType<Boolean> NAKED = AttachmentRegistry.<Boolean>create(
		ClothesThief.id("naked"),
		b -> b.persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
	);

	/** The clothes item currently worn in the clothing slot (empty = none). */
	public static final AttachmentType<ItemStack> WORN = AttachmentRegistry.<ItemStack>create(
		ClothesThief.id("worn"),
		b -> b.persistent(ItemStack.OPTIONAL_CODEC)
			.syncWith(ItemStack.OPTIONAL_STREAM_CODEC, AttachmentSyncPredicate.all())
			.copyOnDeath()
	);

	public static final DataComponentType<ClothesData> CLOTHES_DATA = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		ClothesThief.id("clothes_data"),
		DataComponentType.<ClothesData>builder()
			.persistent(ClothesData.CODEC)
			.networkSynchronized(ClothesData.STREAM_CODEC)
			.build()
	);

	public static final Item CLOTHES = registerClothes();

	private static Item registerClothes() {
		Identifier id = ClothesThief.id("clothes");
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
		return Registry.register(BuiltInRegistries.ITEM, key,
			new ClothesItem(new Item.Properties().setId(key).stacksTo(1)));
	}

	private ModContent() {
	}

	/** Touch the class so everything above gets registered. */
	public static void init() {
	}
}
