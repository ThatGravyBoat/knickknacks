package tech.thatgravyboat.knickknacks.common.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import tech.thatgravyboat.knickknacks.Knickknacks;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Knickknacks.MODID);

    public static final Supplier<DataComponentType<Block>> MINERS_GOGGLES_DATA = DATA_COMPONENTS.registerComponentType(
            "miners_goggles_data",
            builder -> builder
                    .persistent(BuiltInRegistries.BLOCK.byNameCodec())
                    .networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK))
    );

    public static final Supplier<DataComponentType<Integer>> KILLERS_EYE_DATA = DATA_COMPONENTS.registerComponentType(
            "killers_eye_data",
            builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
    );
}
