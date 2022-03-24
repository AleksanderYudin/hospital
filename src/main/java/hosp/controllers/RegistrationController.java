package hosp.controllers;


import hosp.service.UserService;
import hosp.model.entity.Employee;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Model model) {
        model.addAttribute("employee", new Employee());
        return "registration";
    }

    @PostMapping
    public String addUser(@ModelAttribute("employee") @Valid Employee employee, BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()) return "registration";
        if(!userService.addUser(employee)) {
            model.addAttribute("employee", new Employee());
            model.addAttribute("message",
                    "Пользователь с таким логином уже существует!");
            return "registration";
        }
        else {
            model.addAttribute("employee", new Employee());
            model.addAttribute("message",
                    "Пользователь успешно добавлен!");
            return "registration";
        }
    }
}
