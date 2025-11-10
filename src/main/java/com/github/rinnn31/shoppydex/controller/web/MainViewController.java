package com.github.rinnn31.shoppydex.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {
    @GetMapping("/")
    public String homeView() {
        return "home.html";
    }

    @GetMapping("/san-pham")
    public String productsView() {
        return "products.html";
    }
    @GetMapping("/san-pham-chi-tiet")
    public String productDetailView() {
        return "product-info.html";
    }

    @GetMapping("/don-hang")
    public String ordersView() {
        return "orders.html";
    }

    @GetMapping("/quan-ly")
    public String productsManagerView() {
        return "product-manager.html";
    }

    @GetMapping("/nap-tien")
    public String chargePointView() {
        return "recharge.html";
    }

    @GetMapping("/thong-tin")
    public String userInfoView() {
        return "user-info.html";
    }
}
