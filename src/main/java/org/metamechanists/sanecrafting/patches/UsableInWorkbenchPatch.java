package org.metamechanists.sanecrafting.patches;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.experimental.UtilityClass;
import org.metamechanists.sanecrafting.patches.usable.in.workbench.CraftingTableListener;

import java.util.HashSet;
import java.util.Set;


@UtilityClass
public class UsableInWorkbenchPatch {
    public void apply() {
        Set<String> usableInWorkbench = new HashSet<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            if (item.isUseableInWorkbench()) {
                usableInWorkbench.add(item.getId());
            }
            item.setUseableInWorkbench(true);
        }

        CraftingTableListener.register(usableInWorkbench);
    }
}
