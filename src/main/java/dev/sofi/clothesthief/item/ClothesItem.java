package dev.sofi.clothesthief.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import dev.sofi.clothesthief.Outfits;

public class ClothesItem extends Item {
	public ClothesItem(Properties properties) {
		super(properties);
	}

	/** Right-click to put the clothes on (only if you are naked and the clothing slot is free). */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (!Outfits.isStripped(player)) {
			if (!level.isClientSide()) {
				player.sendSystemMessage(Component.literal("You must be naked to put these on."));
			}
			return InteractionResult.FAIL;
		}
		if (!level.isClientSide()) {
			ItemStack stack = player.getItemInHand(hand);
			Outfits.setWorn(player, stack.copyWithCount(1));
			stack.shrink(1);
		}
		return InteractionResult.SUCCESS;
	}
}
