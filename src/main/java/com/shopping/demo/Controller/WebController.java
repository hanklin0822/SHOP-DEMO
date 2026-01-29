package com.shopping.demo.Controller;

import com.shopping.demo.DTO.OrderDTO;
import com.shopping.demo.Entity.Product;
import com.shopping.demo.Entity.User;
import com.shopping.demo.Service.OrderService;
import com.shopping.demo.Service.ProductService;
import com.shopping.demo.Service.UserService;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    public WebController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model){
        List<Product> products=productService.getAllProducts();
        model.addAttribute("products",products);
        return "index";
    }

    @GetMapping("/productlist")
    public String showProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "productlist";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        Product product =new Product();
        model.addAttribute("product",product);
        return "create";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product){
        productService.saveProduct(product);
        return "redirect:/productlist";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Product> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            model.addAttribute("product", optionalProduct.get());
            return "edit";
        } else {
            return "redirect:/"; // 如果找不到商品，導回列表頁
        }
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Integer id, @ModelAttribute Product product) {
        product.setId(id); // 確保 ID 被正確設定
        productService.saveProduct(product);
        return "redirect:/productlist"; // 重新導向回列表頁
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return "redirect:/productlist";

    }

    @GetMapping("/orders")
    public String getOrderListPage(Model model){
        Integer userId=1;
        List<OrderDTO> orderDTOS=orderService.getOrdersByUserId(userId);
        model.addAttribute("orders",orderDTOS);
        return "orderList";
    }
    
    @GetMapping("/orders/{orderId}")
    public String gerOrderDetailPage(@PathVariable Integer orderId,Model model){
        System.out.println("接收請求 查詢訂單ID"+orderId);
        OrderDTO orderDTO=orderService.getOrderById(orderId);
        model.addAttribute("order",orderDTO);
        return "orderDetail";

    }

    @GetMapping("/login")
        public String showLoginPage(){
            return "login";
        }
    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")


    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

        String result = userService.userRegister(user);

        if ("註冊成功".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "註冊成功，請登入！");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result); // "使用者已存在"
            return "redirect:/register";
        }
    }

    }



