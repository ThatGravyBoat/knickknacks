package tech.thatgravyboat.knickknacks;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import tech.thatgravyboat.knickknacks.common.items.PerkItem;
import tech.thatgravyboat.knickknacks.common.perks.*;
import tech.thatgravyboat.knickknacks.common.registries.ModCreativeTabs;
import tech.thatgravyboat.knickknacks.common.registries.ModDataComponents;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;

@Mod(Knickknacks.MODID)
public class Knickknacks {

    public static final String MODID = "knickknacks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Knickknacks(IEventBus bus) {
        bus.addListener(Knickknacks::onRegisterCapabilities);

        ModItems.ITEMS.register(bus);
        ModDataComponents.DATA_COMPONENTS.register(bus);
        ModCreativeTabs.TABS.register(bus);

        NeoForge.EVENT_BUS.addListener(ArchersGlove::onBowUse);
        NeoForge.EVENT_BUS.addListener(NewtonsNull::onKnockback);
        NeoForge.EVENT_BUS.addListener(LifeNeedle::onDamage);
        NeoForge.EVENT_BUS.addListener(MinersGoggles::useInAir);
        NeoForge.EVENT_BUS.addListener(MinersGoggles::useOnBlock);
        NeoForge.EVENT_BUS.addListener(PocketFurnace::onBlockDrops);
        NeoForge.EVENT_BUS.addListener(FarmersAmulet::onFarmlandTrample);
        NeoForge.EVENT_BUS.addListener(KillersEye::onDamage);
        NeoForge.EVENT_BUS.addListener(KillersEye::onKill);

        CuriosApi.registerCurioPredicate(id("predicate"), slot -> slot.stack().getItem() instanceof PerkItem);
    }

    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        for (var entry : ModItems.ITEMS.getEntries()) {
            if (entry.get() instanceof PerkItem item) {
                event.registerItem(CuriosCapability.ITEM, (stack, ignored) -> item.createPerk(stack), item);
            }
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
