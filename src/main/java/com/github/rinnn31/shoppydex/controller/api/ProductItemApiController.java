package com.github.rinnn31.shoppydex.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.rinnn31.shoppydex.model.api.ApiResponse;
import com.github.rinnn31.shoppydex.model.api.ProductItemModel;
import com.github.rinnn31.shoppydex.service.ProductService;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/product/items")
public class ProductItemApiController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ApiResponse<?> deleteAllItemsInProduct(@RequestParam("productId") Long productId) {
        productService.deleteAllProductItems(productId);
        return ApiResponse.success(null);
    } 

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<?> addProductItems(@RequestParam("productId") Long productId,
            @Valid @RequestBody(required = false) ProductItemModel productItemModel,
            @RequestBody(required = false) List<String> listItems)  {
        
        // Nếu body có định dạng  ProductItemModel thì thêm 1 sản phẩm đơn loại UNIQUE_TYPE
        // Nếu body có định dạng List<String> thì thêm nhiều sản phẩm loại MULTI_TYPE
        if (productItemModel != null) {
            productService.addUniqueTypeItem(productId, productItemModel);
            return ApiResponse.success(null);
        } else if (listItems != null) {
            productService.addMultiTypeItems(productId, listItems);
            return ApiResponse.success(null);
        } else {
            return ApiResponse.error(400, "Yêu cầu không hợp lệ");
        }
    }

    @GetMapping
    public ApiResponse<?> getProductItems(@RequestParam("productId") Long productId) {
        boolean adminView = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        boolean isMultiTypeProduct = productService.isMultiTypeProduct(productId);
        if (!isMultiTypeProduct) {
            List<ProductItemModel> items = productService.getUniqueTypeItems(productId, adminView);
            return ApiResponse.success(items);
        } else {
            if (adminView) {
                List<String> itemsList = productService.getMultiTypeItems(productId);
                return ApiResponse.success(itemsList);
            } else {
                return ApiResponse.error(403, "Chỉ admin mới có thể xem sản phẩm loại MULTI_ITEM");
            }
        }
    }
    
    @GetMapping("/{id}")
    public ApiResponse<?> getProductItemById(@PathParam(value = "id") Long itemId) {
        boolean adminView = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        ProductItemModel item = productService.getProductItemById(itemId, adminView);
        return ApiResponse.success(item);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ApiResponse<?> updateProductItem(@PathParam(value = "id") Long itemId,  @RequestBody ProductItemModel productItemModel) {
        productService.updateUniqueTypeItem(productItemModel);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteProductItem(@PathParam(value = "id") Long itemId) {
        productService.deleteUniqueTypeItem(itemId);
        return ApiResponse.success(null);
    }
}
