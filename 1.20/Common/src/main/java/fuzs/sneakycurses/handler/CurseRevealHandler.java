package fuzs.sneakycurses.handler;

import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.capability.CurseRevealCapability;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.init.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Objects;

public class CurseRevealHandler {
    public static final String TAG_CURSES_REVEALED = SneakyCurses.id("curses_revealed").toString();

    public static void onLivingEquipmentChange(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack oldItemStack, ItemStack newItemStack) {
        if (equipmentSlot.isArmor() && ModRegistry.CURSE_REVEAL_CAPABILITY.get(entity).isAllowedToRevealCurses()) {
            if (entity.getRandom().nextDouble() < SneakyCurses.CONFIG.get(ServerConfig.class).curseRevealChance) {
                revealAllCurses(newItemStack);
            }
        }
    }

    public static EventResult onAnvilUpdate(ItemStack leftInput, ItemStack rightInput, MutableValue<ItemStack> output, String itemName, MutableInt enchantmentCost, MutableInt materialCost, Player player) {
        if (isAffected(leftInput) && rightInput.is(ModRegistry.REVEALS_CURSES_ITEM_TAG) && !allCursesRevealed(leftInput)) {
            ItemStack itemStack = leftInput.copy();
            revealAllCurses(itemStack);
            output.accept(itemStack);
            enchantmentCost.accept(SneakyCurses.CONFIG.get(ServerConfig.class).revealCursesCost);
            return EventResult.INTERRUPT;
        }
        return EventResult.PASS;
    }

    public static EventResult onLivingTick(LivingEntity entity) {
        ModRegistry.CURSE_REVEAL_CAPABILITY.maybeGet(entity).ifPresent(CurseRevealCapability::tick);
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (Mob.getEquipmentSlotForItem(itemStack) == equipmentSlot && anyEnchantIsCursed(itemStack) && !allCursesRevealed(itemStack)) {
                if (entity.tickCount % 1200 == 0 && !entity.level().isClientSide) {
                    revealAllCurses(itemStack);
                    entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                    if (entity instanceof Player player) {
                        player.displayClientMessage(Component.translatableWithFallback("test", "Curses revealed for %s", itemStack.getDisplayName()).withStyle(ChatFormatting.RED), false);
                    }
                    break;
                }
            }
        }
        return EventResult.PASS;
    }

    public static void revealAllCurses(ItemStack itemStack) {
        if (anyEnchantIsCursed(itemStack)) {
            CompoundTag tag = itemStack.getTag();
            if (tag == null || !tag.getBoolean(TAG_CURSES_REVEALED)) {
                tag = itemStack.getOrCreateTag();
                tag.putBoolean(TAG_CURSES_REVEALED, true);
            }
        }
    }

    public static boolean allCursesRevealed(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        return tag != null && tag.contains(TAG_CURSES_REVEALED, Tag.TAG_BYTE) && tag.getBoolean(TAG_CURSES_REVEALED);
    }

    public static boolean anyEnchantIsCursed(ItemStack itemStack) {
        return !itemStack.isEmpty() && EnchantmentHelper.getEnchantments(itemStack).keySet().stream().filter(Objects::nonNull).anyMatch(Enchantment::isCurse);
    }

    public static boolean isAffected(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EnchantedBookItem) {
            if (!SneakyCurses.CONFIG.get(ServerConfig.class).affectBooks) {
                return false;
            }
        }
        return anyEnchantIsCursed(itemStack);
    }
}
