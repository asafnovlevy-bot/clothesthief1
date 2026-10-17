package dev.sofi.clothesthief.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

import dev.sofi.clothesthief.ClothesData;
import dev.sofi.clothesthief.ClothesThief;
import dev.sofi.clothesthief.ModContent;
import dev.sofi.clothesthief.Outfits;

public final class OutfitClient {
	public static final Identifier NAKED_PLAYER = ClothesThief.id("textures/entity/naked_player.png");
	public static final Identifier NAKED_VILLAGER = ClothesThief.id("textures/entity/naked_villager.png");
	public static final Identifier VILLAGER_ROBE = ClothesThief.id("textures/entity/villager_robe.png");

	/** Texture chosen for an avatar this frame (null = vanilla). */
	public static final RenderStateDataKey<Identifier> AVATAR_TEXTURE =
		RenderStateDataKey.create(() -> "clothesthief avatar texture");
	/** Whether a villager is rendered naked this frame. */
	public static final RenderStateDataKey<Boolean> VILLAGER_NAKED =
		RenderStateDataKey.create(() -> "clothesthief villager naked");

	private static final Map<UUID, Identifier> SKIN_CACHE = new HashMap<>();

	private OutfitClient() {
	}

	/** Texture override for a player, or null to use their own skin. */
	public static @Nullable Identifier avatarTexture(Entity player) {
		ItemStack worn = Outfits.getWorn(player);
		if (!worn.isEmpty()) {
			return textureFor(worn.get(ModContent.CLOTHES_DATA));
		}
		return Outfits.isNaked(player) ? NAKED_PLAYER : null;
	}

	private static Identifier textureFor(@Nullable ClothesData data) {
		if (data == null) {
			return NAKED_PLAYER;
		}
		return data.villager() ? VILLAGER_ROBE : skinOf(data.owner());
	}

	/** Skin of a player: live while they are online, remembered afterwards. */
	private static Identifier skinOf(UUID id) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			PlayerInfo info = connection.getPlayerInfo(id);
			if (info != null) {
				Identifier texture = info.getSkin().body().texturePath();
				SKIN_CACHE.put(id, texture);
				return texture;
			}
		}
		Identifier cached = SKIN_CACHE.get(id);
		return cached != null ? cached : DefaultPlayerSkin.get(id).body().texturePath();
	}
}
