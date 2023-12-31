package com.tampham.controllers.buyer;

import com.tampham.dtos.OrderItemDto;
import com.tampham.enums.TimeLicense;
import com.tampham.models.Product;
import com.tampham.repository.ProductRepository;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductCtr {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/product/productList")
    public String getProducts(Model model){
//        Role role = new Role();
//        role.setAuthority("ADMIN");
//        roleRepository.save(role);

//        Role role = roleRepository.findByAuthority("ADMIN").get();
//        Set<Role> authorities = new HashSet<>();
//        authorities.add(role);
//
//        User user = new User("admin", passwordEncoder.encode("12345"), "Tam Pham", authorities);
//
//        userRepository.save(user);

        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        return "product/product_list";
    }

    @RequestMapping("/product/detail/")
    public String getDetailProduct(@RequestParam("id")Long id, Model model){
        if (id != null){
            Product product = productRepository.findById(id).get();

            List<TimeLicense> timeLicenses = List.of(TimeLicense.values());

            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProduct(product);

            model.addAttribute("timeLicenses", timeLicenses);
            model.addAttribute("product", product);
            model.addAttribute("orderItemDto", orderItemDto);
            return "product/product_detail";
        }

        return "product/product_list";
    }
}
