package ro.itschool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.entity.Contact;
import ro.itschool.entity.MyUser;
import ro.itschool.repository.ContactRepository;

@Controller
public class ContactController {

    @Autowired
    ContactRepository contactRepository;

    @GetMapping("/contact-message")
    public String getContact(Model model, String keyword) {
        model.addAttribute("contacts", contactRepository.searchContact(keyword));
        return "/contact-message";
    }

    @GetMapping("/contact")
    public String saveContact(Model model) {
        model.addAttribute("contactObject", new Contact());
        return "contact";
    }

    //TODO
    @PostMapping("/contact")
    public String saveContact2(@ModelAttribute Contact contact, Model model, MyUser user) throws Exception {
        model.addAttribute("contactObject", contact);
        if (contact.getUsername().equals((user.getUsername()))){
            contactRepository.save(contact);
            return "redirect:/contact";
        } else
            throw new Exception("Wrong username");
    }

    @RequestMapping(path = "/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") Integer id) {
        contactRepository.deleteById(id);
        return "redirect:/contact-message";
    }
}