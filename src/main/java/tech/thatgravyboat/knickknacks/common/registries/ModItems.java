package tech.thatgravyboat.knickknacks.common.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.items.HammerItem;
import tech.thatgravyboat.knickknacks.common.items.PerkItem;
import tech.thatgravyboat.knickknacks.common.perks.*;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;

import java.util.function.Function;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Knickknacks.MODID);

    public static final DeferredItem<Item> SCRAP = ITEMS.register("scrap", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HAMMER = ITEMS.register("hammer", () -> new HammerItem(new Item.Properties().durability(128)));
    public static final DeferredItem<Item> UPGRADE = ITEMS.register("upgrade", () -> new Item(new Item.Properties()));

    public static final DeferredItem<PerkItem> ARACHERS_GLOVE = register("archers_glove", ArchersGlove::new);
    public static final DeferredItem<PerkItem> NEWTONS_NULL = register("newtons_null", NewtonsNull::new);
    public static final DeferredItem<PerkItem> LIFE_NEEDLE = register("life_needle", LifeNeedle::new);
    public static final DeferredItem<PerkItem> ALCHEMIC_INFUSER = register("alchemic_infuser", AlchemicInfuser::new);
    public static final DeferredItem<PerkItem> BALLOON = register("balloon", Balloon::new);
    public static final DeferredItem<PerkItem> MINERS_GOGGLES = register("miners_goggles", MinersGoggles::new);
    public static final DeferredItem<PerkItem> POCKET_FURNACE = register("pocket_furnace", PocketFurnace::new);
    public static final DeferredItem<PerkItem> FARMERS_AMULET = register("farmers_amulet", FarmersAmulet::new);
    public static final DeferredItem<PerkItem> LUCKY_HORSESHOE = register("lucky_horseshoe", LuckyHorseshoe::new);
    public static final DeferredItem<PerkItem> KILLERS_EYE = register("killers_eye", KillersEye::new);
    public static final DeferredItem<PerkItem> ORB_OF_WELLNESS = register("orb_of_wellness", OrbOfWellness::new);

    private static DeferredItem<PerkItem> register(String id, Function<ItemStack, Perk> factory) {
        return ITEMS.register(id, () -> new PerkItem(factory));
    }
}
