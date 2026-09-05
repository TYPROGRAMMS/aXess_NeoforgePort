package net.teekay.axess;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.teekay.axess.network.AxessPacketHandler;
import net.teekay.axess.registry.*;
import org.slf4j.Logger;

@Mod(Axess.MODID)
public class Axess {
    public static final String MODID = "axess";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AXESS_CREATIVE_TAB = CREATIVE_MODE_TABS.register("axess", () -> CreativeModeTab.builder()
            .displayItems((parameters, output) -> {
                for (DeferredHolder<Item, ? extends Item> item : AxessItemRegistry.getEntries()) output.accept(item.get());
                for (DeferredHolder<Block, ? extends Block> block : AxessBlockRegistry.getEntries()) {
                    try { output.accept(block.get().asItem()); } catch (Exception ignored) { }
                }
            })
            .title(Component.translatable("gui." + MODID + ".creative_tab"))
            .icon(() -> new ItemStack(AxessItemRegistry.KEYCARD.get()))
            .build());

    public Axess(IEventBus modEventBus, ModContainer container) {
        AxessConfig.registerConfig(container);
        AxessBlockRegistry.register(modEventBus);
        AxessItemRegistry.register(modEventBus);
        AxessBlockEntityRegistry.register(modEventBus);
        AxessMenuRegistry.register(modEventBus);
        AxessSoundRegistry.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(AxessConfig::onConfigLoading);
        modEventBus.addListener(AxessPacketHandler::registerPayloads);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(net.neoforged.neoforge.event.server.ServerStartingEvent event) {
        LOGGER.info("[aXess] I'm alive!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Networking is registered through RegisterPayloadHandlersEvent.
    }
}
