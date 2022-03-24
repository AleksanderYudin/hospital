package hosp.controllers;

import hosp.model.entity.Employee;
import hosp.model.enums.Role;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String homePage(@AuthenticationPrincipal Employee employee) {
        if(employee == null) return "redirect:/login";
        if(employee.getRole().equals(Role.DOCTOR)) return "redirect:/doctor";
        if(employee.getRole().equals(Role.NURSE)) return "redirect:/nurse";
        return "redirect:/registration";
    }

    @GetMapping("/doctor")
    public String homeDoctor(@AuthenticationPrincipal Employee employee, Model model) {
        model.addAttribute("employee", employee);
        return "/doctor/home_page";
    }

    @GetMapping("/nurse")
    public String homeNurse(@AuthenticationPrincipal Employee employee, Model model) {
        model.addAttribute("employee", employee);
        return "/nurse/home_page";
    }
    @GetMapping("/hospital")
    public String homeDoctor() {
        return "redirect:/";
    }

}
