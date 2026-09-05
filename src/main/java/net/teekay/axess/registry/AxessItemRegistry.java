package net.teekay.axess.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.teekay.axess.Axess;
import net.teekay.axess.item.AccessWrenchItem;
import net.teekay.axess.item.LinkerItem;
import net.teekay.axess.item.keycard.KeycardItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class AxessItemRegistry {
    public static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(Registries.ITEM, Axess.MODID);
    public static final ArrayList<DeferredHolder<Item, Item>> keycards = new ArrayList<>();
    public static final DeferredHolder<Item, Item> KEYCARD = registerItem("keycard", KeycardItem::new);
    public static final DeferredHolder<Item, Item> ACCESS_WRENCH = registerItem("access_wrench", AccessWrenchItem::new);
    public static final DeferredHolder<Item, Item> READER_LINKER = registerItem("linker", LinkerItem::new);
    public static DeferredHolder<Item, Item> registerItem(String id, Supplier<Item> supplier) {
        DeferredHolder<Item, Item> item = DEFERRED_REGISTER.register(id, supplier);
        if (id.contains("keycard")) keycards.add(item);
        return item;
    }
    public static void register(IEventBus eventBus) { DEFERRED_REGISTER.register(eventBus); }
    public static Collection<DeferredHolder<Item, ? extends Item>> getEntries() { return DEFERRED_REGISTER.getEntries(); }
    public static ArrayList<Item> getKeycards() { ArrayList<Item> out = new ArrayList<>(); for (var k : keycards) out.add(k.get()); return out; }
}
