package dev.sofi.clothesthief.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import dev.sofi.clothesthief.Outfits;

/** One-slot container that is backed by the player's WORN attachment. */
public class WornContainer implements Container {
	private final Player player;

	public WornContainer(Player player) {
		this.player = player;
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return Outfits.getWorn(player).isEmpty();
	}

	@Override
	public ItemStack getItem(int slot) {
		return Outfits.getWorn(player);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return removeItemNoUpdate(slot);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		ItemStack stack = Outfits.getWorn(player);
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		Outfits.setWorn(player, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		Outfits.setWorn(player, stack);
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player p) {
		return true;
	}

	@Override
	public void clearContent() {
		Outfits.setWorn(player, ItemStack.EMPTY);
	}
}
