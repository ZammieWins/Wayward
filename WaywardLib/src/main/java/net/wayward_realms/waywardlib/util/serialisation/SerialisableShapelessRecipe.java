package net.wayward_realms.waywardlib.util.serialisation;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;
import java.util.Map;

public class SerialisableShapelessRecipe implements SerialisableRecipe {

    private List<ItemStack> ingredients;
    private ItemStack result;

    public SerialisableShapelessRecipe(ShapelessRecipe recipe) {
        this.ingredients = recipe.getIngredientList();
        this.result = recipe.getResult();
    }

    private SerialisableShapelessRecipe() {}

    @Override
    public ShapelessRecipe toRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(result);
        try {
            recipe.getClass().getDeclaredField("ingredients").set(recipe, ingredients);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return recipe;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    public static SerialisableShapelessRecipe deserialize(Map<String, Object> serialised) {
        SerialisableShapelessRecipe deserialised = new SerialisableShapelessRecipe();
        deserialised.ingredients = (List<ItemStack>) serialised.get("ingredients");
        deserialised.result = (ItemStack) serialised.get("result");
        return deserialised;
    }

}
