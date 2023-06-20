package fr.fms.controllers;

import fr.fms.entities.Contact;
import fr.fms.entities.User;
import fr.fms.repositories.CategoryRepository;
import fr.fms.repositories.ContactRepository;
import fr.fms.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String home(Model model, @RequestParam(name="page", defaultValue="0") int page){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (auth.isAuthenticated()) {
            List<User> users = userRepository.findByUsername(username);
            if (!users.isEmpty()) {
                User userId = users.get(0);
                System.out.println(userId);
            }
        } else {
            PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("contactName").descending());
            Page<Contact> contactsList = contactRepository.findAll(pageRequest);

            model.addAttribute("contacts", contactsList.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("pages", new int[contactsList.getTotalPages()]);
        }


        return "main";
    }
}
