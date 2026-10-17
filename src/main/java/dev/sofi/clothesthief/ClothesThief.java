package dev.sofi.clothesthief;

import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class ClothesThief implements ModInitializer {
	public static final String MOD_ID = "clothesthief";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModContent.init();

		// Sneak + right-click with an empty hand on a player/villager to take their clothes.
		UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> {
			if (hand != InteractionHand.MAIN_HAND || player.isSpectator()
				|| !player.isShiftKeyDown() || !player.getMainHandItem().isEmpty()) {
				return InteractionResult.PASS;
			}
			if (!(entity instanceof Villager) && !(entity instanceof Player)) {
				return InteractionResult.PASS;
			}
			// Rule: only a naked person can take clothes, and both naked => nothing happens.
			if (!Outfits.isStripped(player) || !Outfits.hasClothes(entity)) {
				return InteractionResult.PASS;
			}
			if (level.isClientSide()) {
				return InteractionResult.SUCCESS;
			}

			ItemStack loot;
			ItemStack worn = Outfits.getWorn(entity);
			if (!worn.isEmpty()) {
				loot = worn.copy();
				Outfits.setWorn(entity, ItemStack.EMPTY);
			} else {
				loot = Outfits.makeClothes(entity);
			}
			Outfits.setNaked(entity, true); // victim stays naked (villagers forever)
			Outfits.setWorn(player, loot); // thief puts them on right away
			return InteractionResult.SUCCESS;
		});

		// /undress: take off your own clothes (or the stolen ones you are wearing) as an item.
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(Commands.literal("undress").executes(ctx -> {
				ServerPlayer p = ctx.getSource().getPlayerOrException();
				ItemStack worn = Outfits.getWorn(p);
				if (!worn.isEmpty()) {
					Outfits.setWorn(p, ItemStack.EMPTY);
					p.getInventory().placeItemBackInInventory(worn, Prediction.SERVER_ONLY);
				} else if (!Outfits.isNaked(p)) {
					Outfits.setNaked(p, true);
					p.getInventory().placeItemBackInInventory(Outfits.makeClothes(p), Prediction.SERVER_ONLY);
				} else {
					ctx.getSource().sendFailure(Component.literal("You are already naked."));
					return 0;
				}
				return 1;
			})));
	}
}
