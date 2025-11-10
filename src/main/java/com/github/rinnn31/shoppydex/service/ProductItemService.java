package com.github.rinnn31.shoppydex.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductItemService {
    private static final Logger logger = LoggerFactory.getLogger(ProductItemService.class);

    @Value("${app.products.items.dir}")
    private String itemsDir;

    public List<String> takeItems(String productId, Integer quantity) {
        Path itemsPath = Path.of(itemsDir, productId + ".txt");
        try {
            List<String> allItems = Files.lines(itemsPath).filter(line -> line != null && !line.isBlank()).toList();
            List<String> takenItems = allItems.stream().limit(quantity).toList();
            List<String> remainingItems = allItems.stream().skip(quantity).toList();
            
            Files.write(itemsPath, remainingItems);
            return takenItems;
        } catch (IOException e) {
            logger.error("Error taking items for product {}: {}", productId, e.getMessage());
            return List.of();
        }
    } 

    public int appendItems(String productId, List<String> newItems) {
        Path itemsPath = Path.of(itemsDir, productId + ".txt");
        try {
            List<String> validItems = newItems.stream()
                .filter(item -> item != null && !item.isBlank())
                .toList();
            Files.write(itemsPath, validItems, java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND);
            return validItems.size();
        } catch (IOException e) {
            logger.error("Error appending items for product {}: {}", productId, e.getMessage());
            return 0;
        }
    }

    public int countItems(String productId) {
        Path itemsPath = Path.of(itemsDir, productId + ".txt");
        try (Stream<String> lines = Files.lines(itemsPath)) {
            return (int) lines.filter(line -> line != null && !line.isBlank()).count();
        } catch (IOException e) {
            logger.error("Error counting items for product {}: {}", productId, e.getMessage());
            return 0;
        }
    }

    public void deleteAllItems(String productId) {
        Path itemsPath = Path.of(itemsDir, productId + ".txt");
        try {
            Files.deleteIfExists(itemsPath);
        } catch (IOException e) {
            logger.error("Error deleting items for product {}: {}", productId, e.getMessage());
        }
    }
}
