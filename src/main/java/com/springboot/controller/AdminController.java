package com.springboot.controller;

import com.springboot.entity.Role;
import com.springboot.entity.User;
import com.springboot.repository.RoleRepository;
import com.springboot.service.RoleService;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;


    @GetMapping("/select")
    public String getUsers(ModelMap model) throws SQLException {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("user1", new User());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(1L,"ADMIN"));
        roleSet.add(new Role(2L,"USER"));
        if (model.getAttribute("userById")==null){
            model.addAttribute("userById", new User());
        }
        model.addAttribute("roleSet", roleSet);
        return "select";
    }

    @PostMapping("/select")
    public String insertUser(@ModelAttribute("user") @Valid User user, ModelMap model) throws SQLException {
        userService.addUser(user);
        model.addAttribute("users", userService.getAllUser());
        return "redirect:/admin/select";
    }

    @PostMapping("/edit/{id}")
    public String getUserById(@PathVariable("id") long id, ModelMap model) throws SQLException {
        User user =  userService.getUserById(id);
        model.addAttribute("userById", user);
        model.addAttribute("user1", new User());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(1L,"ADMIN"));
        roleSet.add(new Role(2L,"USER"));
        model.addAttribute("roleSet", roleSet);
        return  "redirect:/admin/select";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") @Valid User user) throws SQLException {
        userService.updateUser(user);
        return "redirect:/admin/select";
    }

    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable("id") long id) throws SQLException {
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return "redirect:/admin/select";
    }
}
