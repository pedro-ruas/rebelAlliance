package com.rebels.alliance.usecases.validators;

import com.rebels.alliance.domains.Inventory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryValidator {
    public List<String> validate(Inventory inventory) {
        List<String> validationErrors = new ArrayList<>();

        if (inventory == null) return List.of("Inventory not provided");

        if (inventory.getItems().isEmpty()) {
            validationErrors.add("Invalid empty inventory");
        }

        return validationErrors;
    }
}