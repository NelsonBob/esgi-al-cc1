package org.sid.web.controller;


import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.sid.entity.Member;
import org.sid.service.MemberService;
import org.sid.web.dto.MemberRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String listUser(Model model, @RequestParam(name = "motCle", defaultValue = "") String motCle,
                           @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<Member> pageUsers = memberService.findByFirstNameLikeOrLastNameLike("%" + motCle + "%", "%" + motCle + "%",
                PageRequest.of(currentPage - 0, pageSize));

        model.addAttribute("listUser", pageUsers.getContent());
        int[] pages = new int[pageUsers.getTotalPages()];
        model.addAttribute("pages", pages);
        model.addAttribute("pageCourante", currentPage);
        model.addAttribute("motCle", motCle);
        return "security/listUser";
    }

    @GetMapping("/infos/{id}")
    public String showUpdateUser(Model model, @PathVariable("id") Long id, Principal principal) {
        Member member = memberService.findByID(id);
        if (member != null) {
            if (member == memberService.findByEmail(principal.getName())) {
                return "redirect:/changeinfos";
            }
            List<String> listEnable = Arrays.asList("true", "false");
            model.addAttribute("listEnable", listEnable);
            List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
            model.addAttribute("listCivilite", listCivilite);
            model.addAttribute("memberForm", memberService.findByID(id));
            return "security/changeinfosAll";
        }
        return "redirect:/member";
    }

    @PostMapping("/infos/{id}")
    public String UpdateUser(Model model, @PathVariable("id") Long code, Member memberForm, BindingResult result) {
        if (result.hasErrors()) {
            memberForm.setId(code);
            return "security/changeinfosAll";
        }
        List<String> listEnable = Arrays.asList("true", "false");
        model.addAttribute("listEnable", listEnable);
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        try {
            Member member = new Member();
            member.setFirstName(memberForm.getFirstName());
            member.setLastName(memberForm.getLastName());
            member.setEmail(memberForm.getEmail());
            member.setCivilite(memberForm.getCivilite());
            member.setTelephone(memberForm.getTelephone());

            if (listEnable.contains("true") == memberForm.isEnabled()) {
                member.setEnabled(true);
            }
            member.setEnabled(false);
            if (!memberForm.getPassword().isEmpty()) {
                memberService.update(memberForm, code);
                model.addAttribute("memberForm", memberService.findAll());
                return "redirect:/member/infos/" + code + "?success";

            }
            memberService.update(memberForm, code);
            model.addAttribute("memberForm", memberService.findAll());
        } catch (Exception e) {

        }
        return "redirect:/member/infos/" + code + "?success";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id, Principal principal) {
        Member member = memberService.findByID(id);
        if (member != null) {
            if (member == memberService.findByEmail(principal.getName())) {
                return "redirect:/member?error";
            }
            memberService.delete(member);
            model.addAttribute("memberForm", memberService.findAll());
            return "redirect:/member?success";
        }

        return "redirect:/member?success";
    }

    @GetMapping("/add")
    public String showRegistrationForm(Model model) {
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        List<String> listEnable = Arrays.asList("true", "false");
        model.addAttribute("listEnable", listEnable);
        model.addAttribute("userFor", new Member());
        model.addAttribute("memberForm", memberService.findTopByOrderByIdDesc());
        return "security/userAdd";
    }

    @PostMapping("/add")
    public String registerUserAccount(MemberRegistrationDto userDto, BindingResult result, Model model) {
        model.addAttribute("userFor", new Member());
        List<String> listCivilite = Arrays.asList("Monsieur", "Madame");
        model.addAttribute("listCivilite", listCivilite);
        Member existing = memberService.findByEmail(userDto.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "Il y a déjà un compte enregistré avec cet e-mail");
        }

        if (result.hasErrors()) {
            return "redirect:/member/add?error";
        }
        memberService.save(userDto);

        return "redirect:/member/add?success";
    }

    @GetMapping("/pass/{id}")
    public String showUpdatePassUser(Model model, @PathVariable("id") Long id, Principal principal) {
        Member member = memberService.findByID(id);
        if (member != null) {

            model.addAttribute("memberForm", memberService.findByID(id));
            return "security/changeinfosPass";
        }
        return "redirect:/member";
    }

    @PostMapping("/pass/{id}")
    public String UpdatePassUser(Model model, @PathVariable("id") Long code, Member memberForm, BindingResult result) {
        if (result.hasErrors()) {
            memberForm.setId(code);
            return "security/changeinfosPass";
        }
        try {
            memberService.updatePassword(memberForm.getPassword(), code);

        } catch (Exception e) {

        }
        return "redirect:/member/pass/" + code + "?success";
    }

    @GetMapping("/password")
    public String showUpdatePassLog(Model model, Principal principal) {
        Member member = memberService.findByEmail(principal.getName());
        if (member != null) {

            model.addAttribute("memberForm", member);
            return "security/changeinfosPass";
        }
        return "redirect:/member";
    }

}

