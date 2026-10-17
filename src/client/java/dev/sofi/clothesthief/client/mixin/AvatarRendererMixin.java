package dev.sofi.clothesthief.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;

import dev.sofi.clothesthief.client.OutfitClient;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
	private void clothesthief$extract(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		state.setData(OutfitClient.AVATAR_TEXTURE, OutfitClient.avatarTexture(entity));
	}

	@Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("HEAD"), cancellable = true)
	private void clothesthief$texture(AvatarRenderState state, CallbackInfoReturnable<Identifier> cir) {
		Identifier override = state.getData(OutfitClient.AVATAR_TEXTURE);
		if (override != null) {
			cir.setReturnValue(override);
		}
	}
}
