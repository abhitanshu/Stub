//package com.menu.recipe.service;
//
//import java.util.List;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.menu.recipe.data.Recipe;
//import com.menu.recipe.repository.RecipeRepository;
//
//@Service
//public class RecipeServiceImpl implements RecipeService {
//
//	@Autowired
//	private RecipeRepository recipeRepository;
//
//	@Override
//	public void addRecipe(Recipe recipe) {
//		// Code to insert the recipe into the database
//		recipeRepository.save(recipe);
//	}
//
//	@Override
//	public void removeRecipe(long id) {
//		recipeRepository.deleteById(id);
//	}
//
//	@Override
//	public List<Recipe> getAllRecipes() {
//		return recipeRepository.findAll();
//	}
//
//	@Override
//	public Optional<Recipe> findById(long id) {
//		return recipeRepository.findById(id);
//	}
//
//	@Override
//	public Optional<List<Recipe>> searchRecipes(String name, String vegetarian, Integer servingSize,
//			String instructions, String ingredients) {
//		return recipeRepository.findRecipes(name, vegetarian, servingSize, instructions, ingredients);
//	}
//
//	@Override
//	public Optional<List<Recipe>> findByServingSizeAndIngredients(int servingSize, String ingredients) {
//		return recipeRepository.findByServingSizeAndIngredients(servingSize, ingredients);
//	}
//
//	@Override
//	public Optional<List<Recipe>> findByInstructionsAndIngredients(String instructions, String ingredient) {
//		return recipeRepository.findByInstructionsAndIngredients(instructions, ingredient);
//
//	}
//
//}
