package dev.sofi.clothesthief.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import dev.sofi.clothesthief.ClothesThief;
import dev.sofi.clothesthief.ModContent;
import dev.sofi.clothesthief.Outfits;

public class WornSlot extends Slot {
	private final Player owner;

	public WornSlot(Player owner, int x, int y) {
		super(new WornContainer(owner), 0, x, y);
		this.owner = owner;
		this.setBackground(ClothesThief.id("container/slot/clothes"));
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.is(ModContent.CLOTHES) && Outfits.isStripped(owner);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}
}
