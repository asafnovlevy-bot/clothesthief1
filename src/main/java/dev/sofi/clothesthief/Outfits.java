package dev.sofi.clothesthief;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.ItemStack;

/** Helpers for reading/writing the outfit state of a player or villager. */
public final class Outfits {
	private Outfits() {
	}

	public static boolean isNaked(Entity e) {
		return e.getAttachedOrElse(ModContent.NAKED, false);
	}

	public static void setNaked(Entity e, boolean naked) {
		if (naked) {
			e.setAttached(ModContent.NAKED, true);
		} else {
			e.removeAttached(ModContent.NAKED);
		}
	}

	public static ItemStack getWorn(Entity e) {
		return e.getAttachedOrElse(ModContent.WORN, ItemStack.EMPTY);
	}

	public static void setWorn(Entity e, ItemStack stack) {
		if (stack.isEmpty()) {
			e.removeAttached(ModContent.WORN);
		} else {
			e.setAttached(ModContent.WORN, stack);
		}
	}

	/** Naked and not wearing any stolen clothes: allowed to steal. */
	public static boolean isStripped(Entity e) {
		return isNaked(e) && getWorn(e).isEmpty();
	}

	/** Has something that can be taken: stolen clothes on, or still has natural clothes. */
	public static boolean hasClothes(Entity e) {
		return !getWorn(e).isEmpty() || !isNaked(e);
	}

	/** Builds a clothes item carrying the identity (skin) of the given entity. */
	public static ItemStack makeClothes(Entity e) {
		ItemStack stack = new ItemStack(ModContent.CLOTHES);
		String name = e.getName().getString();
		stack.set(ModContent.CLOTHES_DATA, new ClothesData(e instanceof Villager, e.getUUID(), name));
		stack.set(DataComponents.CUSTOM_NAME,
			Component.literal(name + "'s clothes").withStyle(style -> style.withItalic(false)));
		return stack;
	}
}
