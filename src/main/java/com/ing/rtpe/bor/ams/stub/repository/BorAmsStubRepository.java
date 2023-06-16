//package com.menu.recipe.repository;
//
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//@Repository
//public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//
//	@Query("SELECT r FROM Recipe r WHERE r.name LIKE %:name% OR r.vegetarian = :vegetarian OR r.servingSize = :servingSize OR r.instructions LIKE %:instructions% OR r.ingredients LIKE %:ingredients%")
//	Optional<List<Recipe>> findRecipes(String name, String vegetarian, int servingSize, String instructions,
//			String ingredients);
//
//	@Query("SELECT r FROM Recipe r WHERE r.servingSize = :servingSize AND r.ingredients LIKE %:ingredient%")
//	Optional<List<Recipe>> findByServingSizeAndIngredients(int servingSize, String ingredient);
//
//	@Query("SELECT r FROM Recipe r WHERE r.instructions LIKE %:instructions% AND r.ingredients LIKE %:ingredient%")
//	Optional<List<Recipe>> findByInstructionsAndIngredients(String instructions, String ingredient);
//
//}
