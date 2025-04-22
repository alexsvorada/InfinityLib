package io.github.mooy1.infinitylib.machines;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.mockbukkit.mockbukkit.MockBukkit;
import io.github.mooy1.infinitylib.core.MockAddon;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestCraftingBlock {

    private static MockAddon addon;
    private static CraftingBlock machine;
    private static ItemStack input1;
    private static ItemStack input2;
    private static ItemStack output;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        addon = MockBukkit.load(MockAddon.class);
        machine = new CraftingBlock(new SubGroup("key", new ItemStack(Material.DIAMOND)),
                new SlimefunItemStack("ID", Material.STONE, "name"),
                RecipeType.ANCIENT_ALTAR, new ItemStack[0]);
        output = CustomItemStack.create(SlimefunItems.SALT.item(), 2);
        input1 = SlimefunItems.COPPER_DUST.item().clone();
        input2 = new ItemStack(Material.NETHERITE_BLOCK, 2).clone();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @Order(0)
    void testRegister() {
        machine.register(addon);
        assertSame(MachineLayout.CRAFTING_DEFAULT, machine.layout);
    }

    @Test
    @Order(1)
    void testAddRecipes() {
        machine.addRecipe(output, input1, input2, null, null, null, null, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> machine.addRecipe(output));
    }

    @Test
    void testProcess() {
        ItemStack[] input = new ItemStack[9];
        input[0] = input1;
        assertNull(machine.getOutput(input));

        input[1] = input2;
        CraftingBlockRecipe recipe = machine.getOutput(input);
        assertNotNull(recipe);

        recipe.consume(input);
        assertEquals(0, input[0].getAmount());
        assertEquals(0, input[1].getAmount());
    }

}
