/*
 * Copyright (c) 2019-2025 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.galacticraft.impl.rocket.recipe.type;

import dev.galacticraft.api.rocket.recipe.RocketPartRecipeSlot;
import dev.galacticraft.api.rocket.recipe.type.RocketPartRecipeType;
import dev.galacticraft.impl.rocket.recipe.config.CenteredPatternedRocketPartRecipeConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CenteredPatternedRocketPartRecipeType extends RocketPartRecipeType<CenteredPatternedRocketPartRecipeConfig> {
    public static final RocketPartRecipeType<CenteredPatternedRocketPartRecipeConfig> INSTANCE = new CenteredPatternedRocketPartRecipeType();

    private CenteredPatternedRocketPartRecipeType() {
        super(CenteredPatternedRocketPartRecipeConfig.CODEC);
    }

    @Override
    public int slots(CenteredPatternedRocketPartRecipeConfig config) {
        return config.slots().size();
    }

    @Override
    public int height(CenteredPatternedRocketPartRecipeConfig config) {
        return config.height();
    }

    @Override
    public void place(@NotNull SlotConsumer consumer, int leftEdge, int rightEdge, int bottomEdge, CenteredPatternedRocketPartRecipeConfig config) {
        if (leftEdge != rightEdge) throw new UnsupportedOperationException();
        List<RocketPartRecipeSlot> slots = config.slots();
        for (int i = 0; i < slots.size(); i++) {
            RocketPartRecipeSlot slot = slots.get(i);
            consumer.createSlot(i, leftEdge + slot.x(), bottomEdge - this.height(config) + slot.y(), (item, tag) -> {
                if (item == null) return true;

                ItemStack stack = new ItemStack(item, 1);
                stack.applyComponents(tag);
                return slot.ingredient().test(stack);
            });
        }
    }

    @Override
    public @NotNull NonNullList<Ingredient> ingredients(CenteredPatternedRocketPartRecipeConfig config) {
        return config.ingredients();
    }

    @Override
    public boolean matches(RecipeInput input, Level level, CenteredPatternedRocketPartRecipeConfig config) {
        List<RocketPartRecipeSlot> slots = config.slots();
        for (int i = 0, n = slots.size(); i < n; i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty() || !slots.get(i).ingredient().test(stack)) {
                return false;
            }
        }
        return true;
    }
}
