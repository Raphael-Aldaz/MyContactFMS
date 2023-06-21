package fr.fms.controllers;

import fr.fms.entities.User;
import fr.fms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/admin")
    public String admin(Model model, @RequestParam(name="page", defaultValue="0") int page) {
        PageRequest pageRequest = PageRequest.of(page, 8);
        Page<User> userList = userRepository.findAll(pageRequest);
        model.addAttribute("userList", userList.getContent());
        return"admin";

}
}
