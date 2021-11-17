package org.sid.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // Url d'acceuil du site
    @GetMapping("/")
    public String root() {
        return "page/home";
    }

    //ca c'est juste pour les tests
    @GetMapping("/e")
    public String h() {
        return "page/h1";
    }

    // Url pour de connection
    @GetMapping("/login")
    public String login(Model model) {
        return "security/login";
    }

    // url de forbidden c'est a dire la page qui
    // s'affiche quand l'utilisateur n'a pas d'authorization
    @GetMapping(value = "/403")
    public String accessDenied() {
        return "security/403";
    }

    // Url qui s'affiche lorsque le login est coreect
    @GetMapping("/login?success")
    public String loginSucces(Model model) {
        return "redirect:/";
    }


    @GetMapping("/fonts/glyphicons-halflings-regular.ttf")
    public String redi(Model model) {
        return "redirect:/";
    }
}
