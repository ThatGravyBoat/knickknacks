package tech.thatgravyboat.knickknacks.mixins.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.knickknacks.common.events.BlockChangeEvent;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(
            method = "markAndNotifyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;onBlockStateChange(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void onBlockChange(BlockPos pos, LevelChunk levelchunk, BlockState oldState, BlockState newState, int p_46607_, int p_46608_, CallbackInfo ci) {
        Level level = (Level) (Object) this;
        NeoForge.EVENT_BUS.post(new BlockChangeEvent(level, pos, oldState, newState));
    }
}
