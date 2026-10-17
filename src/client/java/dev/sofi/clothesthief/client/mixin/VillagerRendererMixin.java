package dev.sofi.clothesthief.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.npc.villager.Villager;

import dev.sofi.clothesthief.Outfits;
import dev.sofi.clothesthief.client.OutfitClient;

@Mixin(VillagerRenderer.class)
public abstract class VillagerRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/npc/villager/Villager;Lnet/minecraft/client/renderer/entity/state/VillagerRenderState;F)V", at = @At("RETURN"))
	private void clothesthief$extract(Villager villager, VillagerRenderState state, float partialTicks, CallbackInfo ci) {
		boolean naked = Outfits.isNaked(villager);
		state.setData(OutfitClient.VILLAGER_NAKED, naked);
		if (naked) {
			// No villager data => the biome and profession clothing layers are skipped.
			state.villagerData = null;
		}
	}

	@Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/VillagerRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("HEAD"), cancellable = true)
	private void clothesthief$texture(VillagerRenderState state, CallbackInfoReturnable<Identifier> cir) {
		if (Boolean.TRUE.equals(state.getData(OutfitClient.VILLAGER_NAKED))) {
			cir.setReturnValue(OutfitClient.NAKED_VILLAGER);
		}
	}
}
