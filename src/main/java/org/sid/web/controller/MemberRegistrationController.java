package org.sid.web.controller;


import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.sid.entity.Member;
import org.sid.service.MemberService;
import org.sid.web.dto.MemberRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class MemberRegistrationController {

    @Autowired
    private MemberService memberService;

    @ModelAttribute("memberForm")
    public MemberRegistrationDto memberRegistrationDto() {
        return new MemberRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        return "security/registration";
    }

    @PostMapping
    public String registerMemberAccount(@ModelAttribute("memberForm") @Valid MemberRegistrationDto memberDto,
                                        BindingResult result, Model model) {

        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        Member existing = memberService.findByEmail(memberDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "Il y a déjà un compte enregistré avec cet e-mail");
        }

        if (result.hasErrors()) {
            return "security/registration";
        }

        memberService.save(memberDto);

        memberService.autologin(memberDto.getEmail(), memberDto.getPassword());
        return "redirect:/";
    }

    @GetMapping("/professionnel")
    public String showRegistrationForProfessionnel(Model model) {
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        return "security/registrationPro";
    }

    @PostMapping("/professionnel")
    public String registerMemberAccountProfessionnel(@ModelAttribute("memberForm") @Valid MemberRegistrationDto memberDto,
                                                     BindingResult result, Model model) {

        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        Member existing = memberService.findByEmail(memberDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "Il y a déjà un compte enregistré avec cet e-mail");
        }

        if (result.hasErrors()) {
            return "security/registrationPro";
        }

        memberService.saveProfessionnel(memberDto);

        memberService.autologin(memberDto.getEmail(), memberDto.getPassword());
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String showRegistrationFormAdmin(Model model) {
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        return "security/registrationAdmin";
    }

    @PostMapping("/admin")
    public String registerMemberAccountAdmin(@ModelAttribute("memberForm") @Valid MemberRegistrationDto memberDto,
                                             BindingResult result, Model model) {

        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        Member existing = memberService.findByEmail(memberDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "Il y a déjà un compte enregistré avec cet e-mail");
        }

        if (result.hasErrors()) {
            return "redirect:/registration/admin";
        }

        memberService.saveAdmin(memberDto);

        memberService.autologin(memberDto.getEmail(), memberDto.getPassword());
        return "redirect:/";
    }

}
