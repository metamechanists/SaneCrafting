package org.metamechanists.sanecrafting;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


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
}
