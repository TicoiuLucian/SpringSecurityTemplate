package ro.itschool.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.itschool.entity.Role;
import ro.itschool.repository.RoleRepository;
import ro.itschool.service.UserService;
import ro.itschool.util.Constants;

@Controller
public class UserController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;


    //--------- GET all users for ADMINs only ------------------------------
    @GetMapping("/users")
    public String getAllUsers(Model model, String keyword) throws Exception {
        model.addAttribute("users", userService.searchUser(keyword));
        model.addAttribute("roles", roleRepository.findAll().stream().map(Role::getName).toList());
        model.addAttribute("adminRole", roleRepository.findAll()
                .stream()
                .map((Role::getName))
                .filter(role -> role.equals(Constants.ROLE_ADMIN))
                .findAny()
                .orElseThrow(() -> new Exception("User with admin roles not found")));

        return "all-users";
    }

    //---------DELETE a user by id for ADMINs only ------------------------------
    @RequestMapping(path = "/delete/{id}")
    public String deleteUserById(Model model, @PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }


}
