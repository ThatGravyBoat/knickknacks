package tech.thatgravyboat.knickknacks.mixins.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import tech.thatgravyboat.knickknacks.common.perks.AlchemicInfuser;

import java.util.function.Consumer;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    @WrapOperation(method = "finishUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/PotionContents;forEachEffect(Ljava/util/function/Consumer;)V"))
    private void onPotionDrank(PotionContents instance, Consumer<MobEffectInstance> mobeffectinstance1, Operation<Void> original, @Local Player player) {
        original.call(instance, (Consumer<MobEffectInstance>) (effect) -> {
            MobEffectInstanceAccessor accessor = (MobEffectInstanceAccessor) effect;
            accessor.setDuration(AlchemicInfuser.onPotionDrank(effect, player));
            mobeffectinstance1.accept(effect);
        });
    }
}
