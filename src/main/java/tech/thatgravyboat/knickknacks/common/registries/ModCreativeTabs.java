package tech.thatgravyboat.knickknacks.common.registries;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.items.PerkItem;

import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Knickknacks.MODID);

    public static final Supplier<CreativeModeTab> KNICKKNACKS = TABS.register("knickknacks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.knickknacks"))
            .icon(() -> new ItemStack(ModItems.LUCKY_HORSESHOE.get()))
            .displayItems((parameters, output) -> {
                for (var entry : ModItems.ITEMS.getEntries()) {
                    if (entry.get() instanceof PerkItem) {
                        for (Rarity value : Rarity.values()) {
                            ItemStack stack = new ItemStack(entry.get());
                            stack.set(DataComponents.RARITY, value);
                            output.accept(stack);
                        }
                    } else {
                        output.accept(entry.get());
                    }
                }
            })
            .build());
}
