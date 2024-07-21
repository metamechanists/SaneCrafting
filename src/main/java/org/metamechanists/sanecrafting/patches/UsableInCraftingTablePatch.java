package org.metamechanists.sanecrafting.patches;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;


@UtilityClass
public class UsableInCraftingTablePatch {
    public void apply() {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            item.setUseableInWorkbench(true);
        }
    }
}
