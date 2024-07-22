package org.metamechanists.sanecrafting.patches;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.sanecrafting.SaneCrafting;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


// Once, I saw the mirror in my dreams. It spoke to me.
// 'Idra, go forth, and take my insights. It is time to apply the Holy principles of Reflection for the betterment of Humanity.'
// I woke up, and wrote this code. My life has been 10x better ever since. Follow the mirror.
@UtilityClass
public class RecipeLorePatch {
    // god almighty what am I doing
    private final RecipeType FAKE_ENHANCED_CRAFTING_TABLE = new FakeCraftingTableType(
            new NamespacedKey(SaneCrafting.getInstance(), "smeltery"),
            SlimefunItems.SMELTERY,
            "man, fuck this");

    class FakeCraftingTableType extends RecipeType {
        FakeCraftingTableType(NamespacedKey key, SlimefunItemStack slimefunItem, String... lore) {
            super(key, slimefunItem, lore);
        }

        // lmao
        @Override
        public @NotNull ItemStack getItem(Player p) {
            return Slimefun.getLocalization().getRecipeTypeItem(p, RecipeType.SMELTERY);
        }
    }

    public void apply() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            Field field = RecipeType.class.getDeclaredField("ENHANCED_CRAFTING_TABLE");
            Object staticFieldBase = unsafe.staticFieldBase(field);
            long staticFieldOffset = unsafe.staticFieldOffset(field);
            unsafe.putObject(staticFieldBase, staticFieldOffset, FAKE_ENHANCED_CRAFTING_TABLE);
            Bukkit.getLogger().severe(RecipeType.ENHANCED_CRAFTING_TABLE.key().namespace());
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
            Bukkit.getLogger().info("Failed to apply ChangeRecipeTypePatch");
            e.printStackTrace();
        }
    }

//    for (
//    SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
//        if (!item.getRecipeType().equals(RecipeType.ENHANCED_CRAFTING_TABLE)) {
//            continue;
//        }
//
//        try {
//            Field field = item.getClass().getField("recipeType");
//            field.setAccessible(true);
//
//            Field modifiersField = Field.class.getDeclaredField("modifiers");
//            modifiersField.setAccessible(true);
//            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//
//            field.set(null, FAKE_ENHANCED_CRAFTING_TABLE);
//        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchFieldException e) {
//            Bukkit.getLogger().info("Failed to apply ChangeRecipeTypePatch");
//            e.printStackTrace();
//        }
//    }
}
