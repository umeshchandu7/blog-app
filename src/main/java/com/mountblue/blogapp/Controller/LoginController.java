package com.mountblue.blogapp.Controller;

import com.mountblue.blogapp.Entity.Role;
import com.mountblue.blogapp.Entity.User;
import com.mountblue.blogapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@Controller
public class LoginController {
    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/loginUser")
    public String showMyLoginPage() {


        return "fancy-login";
    }

    // add request mapping for /access-denied

    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";
    }

    @GetMapping("/register")
    public String registerUser() {
        return "register_form";
    }

    @GetMapping("/processUser")
    public String processUserDetails(@RequestParam("name") String name,
                                     @RequestParam("email") String email,
                                     @RequestParam("pass") String password,
                                     @RequestParam("conpass") String confirmpassword, Model model) {
        if(userService.findByUserName(name)==null)
        {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            Role  role = new Role();
            role.setName("ROLE_AUTHOR");
            Collection<Role> collectionOfRoles = List.of(role);
            user.setRoles(collectionOfRoles);
            userService.saveUser(user);


        }
        else if(!password.equals(confirmpassword))
        {
            return "redirect:/register";
        }
        else
        {
            return "redirect:/register";
        }
      return "redirect:/loginUser";
    }
}
