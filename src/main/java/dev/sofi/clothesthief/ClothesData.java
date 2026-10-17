package dev.sofi.clothesthief;

import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/** Who the clothes belonged to. Stored on the clothes item. */
public record ClothesData(boolean villager, UUID owner, String name) {
	public static final Codec<ClothesData> CODEC = RecordCodecBuilder.create(i -> i.group(
		Codec.BOOL.fieldOf("villager").forGetter(ClothesData::villager),
		UUIDUtil.CODEC.fieldOf("owner").forGetter(ClothesData::owner),
		Codec.STRING.fieldOf("name").forGetter(ClothesData::name)
	).apply(i, ClothesData::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ClothesData> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL, ClothesData::villager,
		UUIDUtil.STREAM_CODEC, ClothesData::owner,
		ByteBufCodecs.STRING_UTF8, ClothesData::name,
		ClothesData::new
	);
}
