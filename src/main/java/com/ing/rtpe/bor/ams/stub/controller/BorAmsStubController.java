package com.ing.rtpe.bor.ams.stub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RestController

public class BorAmsStubController {



	@GetMapping("/getBorAmsStubs")
	@Operation(summary = "get Bor API")

	public String getAllBorAPI() {
		return "return bore";
	}

//	@PostMapping("/recipes")
//	@Operation(summary = "add a new recipe")
//
////	public String createRecipes(@RequestBody Recipe recipe) {
////
////		return "New Recipe Created";
////	}


}
