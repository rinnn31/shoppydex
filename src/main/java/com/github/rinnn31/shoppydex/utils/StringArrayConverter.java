package com.github.rinnn31.shoppydex.utils;

import java.util.List;

import jakarta.persistence.AttributeConverter;

public class StringArrayConverter implements AttributeConverter<List<String>, String> {

    private static final String SEPARATOR = "%a%";

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        if (attribute.stream().anyMatch(s -> s.contains(SEPARATOR))) {
            throw new IllegalArgumentException("Strings in the list cannot contain the separator: " + SEPARATOR);
        }
        
        return String.join(SEPARATOR, attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return List.of(dbData.split(SEPARATOR));
    }
    
}
