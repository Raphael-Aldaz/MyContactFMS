package fr.fms.controllers;

import fr.fms.entities.Category;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * Home Controller. Using for mapping the home page
 */
@Controller
public class HomeController {
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * Mapping the home page
     * @param model
     * @param page
     * @param kw
     * @param idCat
     * @return list of contacts with different parameters and if the user is connected or not
     */
    @GetMapping("/")
    public String home(Model model, @RequestParam(name="page", defaultValue="0") int page,
                       @RequestParam(name="kw", defaultValue="") String kw,
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
            model.addAttribute("user", user);

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

    /**
     * Mapping the contact page
     * @param model
     * @param idContact
     * @return the contact page with the contact to modify or a new contact
     */

    @GetMapping("/contact")
    public String contact(Model model,
                          @RequestParam( required = false ,name = "idContact")  Long idContact){
        List<Category> categories = categoryRepository.findAll();
        Long userId = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User user = userRepository.findByUsername(userDetails.getUsername());

                userId = user.getId();

        }
        if(idContact == null){
            model.addAttribute("newContact", new Contact());
        } else {
            Optional<Contact> optionalContact = contactRepository.findById(idContact);
            if(!optionalContact.isPresent()){
                return "redirect:/";
            } else {
                Contact contact = optionalContact.get();
                model.addAttribute("newContact", contact);
            }
        }
        model.addAttribute("userId", userId);
        model.addAttribute("categories", categories);


        return "contact";
    }

    /**
     * Mapping the save contact page
     * @param newContact
     * @param bindingResult
     * @return the main page
     */
    @PostMapping("/saveContact")
    public String saveContact(Contact newContact, BindingResult bindingResult){

        if(bindingResult.hasErrors()) return "contact";
        if(newContact.getId() == null){
            contactRepository.save(newContact);
        } else {
            Optional<Contact> optionalContact = contactRepository.findById(newContact.getId());
            if(optionalContact.isPresent()){
                Contact contact = optionalContact.get();
                contact.setContactName(newContact.getContactName());
                contact.setContactEmail(newContact.getContactEmail());
                contact.setContactPhone(newContact.getContactPhone());
                contact.setCategory(newContact.getCategory());
                contact.setContactPhoto(newContact.getContactPhoto());
                contactRepository.save(contact);
            }
        }

        return "redirect:/?page=0";
    }

    /**
     * Mapping the delete contact page
     * @param idContact
     * @param page
     * @return the main page
     */
    @GetMapping("/delete-contact")
    public String deleteContact(Long idContact, int page){
        contactRepository.deleteById(idContact);
        return "redirect:/?page="+page;
    }

    /**
     * Mapping the login page
     * @return the login page
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * Mapping the process login page
     * @param username
     * @param password
     * @return the main page
     */
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        return "main";
    }

    /**
     * Mapping the logout page
     * @param request
     * @param response
     * @return the login page
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    /**
     * Mapping the error page
     * @return the error page
     */

    @GetMapping("/error")
    public String pageError() {
        return "404";
    }

    /**
     * Mapping the 403 page (access denied)
     * @return  the 403 page
     */
    @GetMapping("/403")
    public String page403() {
        return "403";
    }

}
