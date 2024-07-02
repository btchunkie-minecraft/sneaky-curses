package fuzs.sneakycurses.client.handler;

import fuzs.puzzleslib.api.client.screen.v2.ScreenHelper;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import fuzs.sneakycurses.util.CurseMatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemTooltipHandler {
    @SubscribeEvent
    public void onItemTooltip(final ItemTooltipEvent evt) {
        Player player = evt.getPlayer();
        ItemStack itemStack = evt.getItemStack();
        if (!CurseMatcher.isAffected(evt.getItemStack()) || !isAffected(player, itemStack))
            return;
        this.dyeHoverName(evt.getToolTip());
        this.hideCurses(evt.getToolTip(), this.getCursesAsTooltip(evt.getItemStack()));
    }

    @SuppressWarnings("deprecation")
    private void dyeHoverName(List<Component> tooltip) {
        if (!SneakyCurses.CONFIG.client().colorName)
            return;
        TextComponent hoverName = null;
        for (Component value : tooltip) {
            if (value instanceof TextComponent component && component.getText().equals("")) {
                hoverName = component;
                break;
            }
        }
        if (hoverName != null) {
            hoverName.withStyle(ChatFormatting.RED);
        }
    }

    @SuppressWarnings("deprecation")
    private void hideCurses(List<Component> tooltip, List<String> curses) {
        if (!SneakyCurses.CONFIG.client().hideCurses)
            return;
        // use this approach for compatibility with enchantment descriptions mod as this
        // also matches their description key format
        Iterator<Component> iterator = tooltip.iterator();
        while (iterator.hasNext()) {
            Component component = iterator.next();
            if (component instanceof TranslatableComponent translatableComponent
                    && translatableComponent.getKey().startsWith("enchantment")) {
                for (String curse : curses) {
                    if (translatableComponent.getKey().startsWith(curse)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isAffected(Player player, ItemStack itemStack) {
        Level level = player.getLevel();
        if (player != null && level.isClientSide) {
            if (itemStack.isEmpty()) {
                return false;
            }
            // show when holding shift in creative mode
            Minecraft minecraft = Minecraft.getInstance();
            MultiPlayerGameMode gameMode = minecraft.gameMode;
            if (gameMode == null) {
                return false;
            }
            if (gameMode.hasInfiniteItems() &&
                    Screen.hasShiftDown() &&
                    SneakyCurses.CONFIG.server().shiftShows) {
                return false;
            } else if (itemStack.getItem() instanceof EnchantedBookItem &&
                    !SneakyCurses.CONFIG.server().affectBooks) {
                return false;
            }
            // don't show in anvil output slot, since it would reveal curses without
            // actually having to apply the operation
            if (minecraft.screen instanceof AnvilScreen screen) {
                Slot hoveredSlot = ScreenHelper.INSTANCE.getHoveredSlot(screen);
                AnvilMenu anvilMenu = screen.getMenu();
                Slot resultSlot = anvilMenu.getSlot(AnvilMenu.RESULT_SLOT);
                if (hoveredSlot != null &&
                        resultSlot.index == hoveredSlot.index &&
                        hoveredSlot.getItem().equals(itemStack)) {
                    Slot inputSlot = screen.getMenu().getSlot(0);
                    if (!CurseRevealHandler.allCursesRevealed(inputSlot.getItem())) {
                        return true;
                    }
                }
            }
            return CurseRevealHandler.isAffected(itemStack) && !CurseRevealHandler.allCursesRevealed(itemStack);
        }
        return false;
    }

    private List<String> getCursesAsTooltip(ItemStack stack) {
        return EnchantmentHelper.getEnchantments(stack).keySet().stream()
                .filter(Objects::nonNull)
                .filter(Enchantment::isCurse)
                .map(Enchantment::getDescriptionId)
                .collect(Collectors.toList());
    }
}
