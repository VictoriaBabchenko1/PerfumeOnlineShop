package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.domain.OrderStatus;
import org.ecommerce.onlineshop.service.PerfumeService;
import org.ecommerce.onlineshop.service.OrderService;
import org.ecommerce.onlineshop.service.PaymentService;
import org.ecommerce.onlineshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
public class AdminController {
    private final UserService userService;
    private final PerfumeService perfumeService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public AdminController(UserService userService, PerfumeService perfumeService, OrderService orderService, PaymentService paymentService) {
        this.userService = userService;
        this.perfumeService = perfumeService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/admin/perfumes")
    public String showAllPerfumes(Model model) {
        model.addAttribute("perfumes", perfumeService.getAllPerfumes());

        return "admin-perfumes";
    }

    @GetMapping("/admin/orders")
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrdersSortedByDateDesc());
        model.addAttribute("statuses", OrderStatus.values());

        return "admin-orders";
    }

    @PostMapping("/admin/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderId,
                                    @RequestParam String status,
                                    Model model) {
        orderService.updateOrderStatus(orderId, OrderStatus.valueOf(status));
        model.addAttribute("message", "Order " + orderId + " status updated to " + status);
        model.addAttribute("orders", orderService.getAllOrdersSortedByDateDesc());
        model.addAttribute("statuses", OrderStatus.values());

        return "admin-orders";
    }

    @PostMapping("/admin/perfumes/delete")
    public String  deletePerfume(@RequestParam(required = true, defaultValue = "" ) Long perfumeId, Model model) {
        String message = perfumeService.deletePerfume(perfumeId);

        model.addAttribute("message", message);
        model.addAttribute("perfumes", perfumeService.getAllPerfumes());

        return "admin-perfumes";
    }

    @GetMapping("/admin/perfume/add")
    public String showAddPerfumeForm() {
        return "admin-add-perfume";
    }

    @GetMapping("/admin/perfume/edit")
    public String showEditPerfumeForm(@RequestParam Long perfumeId, Model model) {
        Perfume perfume = perfumeService.getPerfumeById(perfumeId);
        model.addAttribute("perfume", perfume);
        return "admin-edit-perfume";
    }

    @PostMapping("/admin/perfume/edit")
    public String editPerfume(@RequestParam Long perfumeId,
                             @RequestParam String title,
                             @RequestParam String brand,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @RequestParam String year,
                             @RequestParam String country,
                             @RequestParam String gender,
                             @RequestParam String description,
                             @RequestParam String price,
                             @RequestParam String volume,
                             @RequestParam String type,
                             @RequestParam String fragranceNotes,
                             Model model) {
        Perfume perfume = new Perfume(title, brand, Integer.parseInt(year), country,
            gender, description, new BigDecimal(price), Integer.parseInt(volume), type, fragranceNotes);
        String message = perfumeService.updatePerfume(perfumeId, perfume, image);

        model.addAttribute("message", message);
        model.addAttribute("perfumes", perfumeService.getAllPerfumes());

        return "admin-perfumes";
    }

    @PostMapping("/admin/perfume/add")
    public String addPerfume(@RequestParam String title,
                             @RequestParam String brand,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam String year,
                             @RequestParam String country,
                             @RequestParam String gender,
                             @RequestParam String description,
                             @RequestParam String price,
                             @RequestParam String volume,
                             @RequestParam String type,
                             @RequestParam String fragranceNotes,
                             Model model) {
        Perfume perfume = new Perfume(title, brand, Integer.parseInt(year), country,
            gender, description, new BigDecimal(price), Integer.parseInt(volume), type, fragranceNotes);
        String message = perfumeService.savePerfume(perfume, image);

        model.addAttribute("message", message);
        model.addAttribute("perfumes", perfumeService.getAllPerfumes());

        return "admin-perfumes";
    }

    @GetMapping("/admin/users")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());

        return "admin-users";
    }

    @PostMapping("/admin/users/delete")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId, Model model) {
        String message = userService.deleteUser(userId);

        model.addAttribute("message", message);
        model.addAttribute("allUsers", userService.allUsers());

        return "admin-users";
    }

    @PostMapping("/admin/users/reset-password")
    public String resetUserPassword(@RequestParam Long userId, Model model) {
        String message = userService.resetUserPassword(userId);

        model.addAttribute("message", message);
        model.addAttribute("allUsers", userService.allUsers());

        return "admin-users";
    }

    @GetMapping("/admin/superAdmin/admins")
    public String showAllAdmins(Model model) {
        model.addAttribute("admins", userService.allAdmins());

        return "superAdmin-allAdmins";
    }

    @PostMapping("/admin/superAdmin/admins/delete")
    public String  deleteAdmin(@RequestParam(required = true, defaultValue = "" ) Long userId, Model model) {
        String message = userService.deleteUser(userId);

        model.addAttribute("message", message);
        model.addAttribute("admins", userService.allAdmins());

        return "superAdmin-allAdmins";
    }

    @PostMapping("/admin/superAdmin/admins/reset-password")
    public String resetAdminPassword(@RequestParam Long userId, Model model) {
        String message = userService.resetUserPassword(userId);

        model.addAttribute("message", message);
        model.addAttribute("admins", userService.allAdmins());

        return "superAdmin-allAdmins";
    }

    @PostMapping("/admin/superAdmin/users/promote-to-admin")
    public String promoteToAdmin(@RequestParam Long userId, Model model) {
        String message = userService.promoteToAdmin(userId);

        model.addAttribute("message", message);
        model.addAttribute("allUsers", userService.allUsers());

        return "admin-users";
    }

    @PostMapping("/admin/superAdmin/admins/demote-to-user")
    public String demoteToRegularUser(@RequestParam Long userId, Model model) {
        String message = userService.demoteToRegularUser(userId);

        model.addAttribute("message", message);
        model.addAttribute("admins", userService.allAdmins());

        return "superAdmin-allAdmins";
    }
    
    @GetMapping("/admin/superAdmin/finance")
    public String showFinancePage(Model model) {
        Long totalOrders = orderService.getTotalOrdersCount();
        BigDecimal totalRevenueFromOrders = orderService.getTotalRevenueFromOrders();
        BigDecimal averageOrderValue = orderService.getAverageOrderValue();

        BigDecimal totalRevenueFromPayments = paymentService.getTotalRevenue();
        Long totalCompletedPayments = paymentService.getTotalCompletedPayments();

        model.addAttribute("orderCountsByStatus", orderService.getOrderCountsByStatus());
        model.addAttribute("orderAmountsByStatus", orderService.getOrderAmountsByStatus());

        model.addAttribute("paymentCountsByStatus", paymentService.getPaymentCountsByStatus());
        model.addAttribute("paymentAmountsByStatus", paymentService.getPaymentAmountsByStatus());

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenueFromOrders", totalRevenueFromOrders);
        model.addAttribute("totalRevenueFromPayments", totalRevenueFromPayments);
        model.addAttribute("averageOrderValue", averageOrderValue);
        model.addAttribute("totalCompletedPayments", totalCompletedPayments);
        
        return "admin-finance";
    }
}
