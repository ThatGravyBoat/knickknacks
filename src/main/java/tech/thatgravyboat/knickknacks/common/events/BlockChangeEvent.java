package tech.thatgravyboat.knickknacks.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockChangeEvent extends BlockEvent {

    private final BlockState oldState;

    public BlockChangeEvent(Level level, BlockPos pos, BlockState oldState, BlockState newState) {
        super(level, pos, newState);
        this.oldState = oldState;
    }

    public BlockState getOldState() {
        return oldState;
    }
}
