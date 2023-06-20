package fr.fms.controllers;

import fr.fms.entities.Contact;
import fr.fms.entities.User;
import fr.fms.repositories.CategoryRepository;
import fr.fms.repositories.ContactRepository;
import fr.fms.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @GetMapping("/")
    public String home(Model model, @RequestParam(name="page", defaultValue="0") int page,
                       @RequestParam(name="keyword", defaultValue="") String kw,
                       @RequestParam(name="idCat", defaultValue="0") Long idCat) {

        Page<Contact> contactsList = null;
        Page<Contact> contactsListOfUser = null;
        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by("contactName").descending());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();


        if (principal instanceof UserDetails) {

            UserDetails userDetails = (UserDetails) principal;
            User user = userRepository.findByUsername(userDetails.getUsername());

            if(!kw.isEmpty()) {
                contactsListOfUser = contactRepository.findByContactNameContainsAndUserId(kw,user.getId(), pageRequest);
            } else if(idCat > 0) {
                contactsListOfUser = contactRepository.findByCategoryIdAndUserId(idCat,user.getId(), pageRequest);
            } else {
                contactsListOfUser = contactRepository.findByUserId(user.getId(), pageRequest);
            }

        } else if (principal instanceof String) {

            if(!kw.isEmpty()) {
                contactsList = contactRepository.findByContactNameContains(kw, pageRequest);
            } else if(idCat > 0) {
                contactsList = contactRepository.findByCategoryId(idCat, pageRequest);
            } else {
                contactsList = contactRepository.findAll(pageRequest);
            }
        } else {
            logger.warn("Unknown principal type: ", principal.getClass());
        }

        Page<Contact> contactsToUse = (contactsListOfUser != null) ? contactsListOfUser : contactsList;
        assert contactsToUse != null;
        model.addAttribute("contacts", contactsToUse.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pages", new int[contactsToUse.getTotalPages()]);
        return "main";



    }
}
