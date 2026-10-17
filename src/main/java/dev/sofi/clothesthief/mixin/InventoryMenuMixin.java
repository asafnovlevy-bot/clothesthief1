package dev.sofi.clothesthief.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;

import dev.sofi.clothesthief.slot.WornSlot;

/** Adds the clothing slot to the player inventory screen (above the off-hand slot). */
@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {
	protected InventoryMenuMixin(MenuType<?> type, int containerId) {
		super(type, containerId);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void clothesthief$addClothingSlot(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
		// Appended last, so vanilla slot indices are untouched.
		this.addSlot(new WornSlot(owner, 77, 44));
	}
}
