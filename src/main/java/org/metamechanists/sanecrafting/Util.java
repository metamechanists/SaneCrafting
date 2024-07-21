package org.metamechanists.sanecrafting;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@UtilityClass
public class Util {
    public @NotNull String generateRecipeId(@NotNull ItemStack output, int recipeIndex) {
        // Use index to avoid ID clash if two recipes for same item
        String normalisedName = PlainTextComponentSerializer.plainText()
                .serialize(output.displayName())
                .toLowerCase()
                .replace(' ', '_')
                .replaceAll("[^a-z0-9/._\\-]", ""); // remove characters not allowed in id
        return "sanecrafting_" + recipeIndex + "_" + normalisedName;
    }

    public @Nullable <T extends SlimefunItem> T findMultiblock(Class<T> clazz) {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (clazz.isInstance(item)) {
                return clazz.cast(item);
            }
        }

        Bukkit.getLogger().severe("Failed to initialise SaneCrafting; EnhancedCraftingTable does not exist!");
        return null;
    }
}
