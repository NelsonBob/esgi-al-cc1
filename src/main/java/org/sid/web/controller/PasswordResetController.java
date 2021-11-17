package org.sid.web.controller;

import javax.validation.Valid;

import org.sid.entity.Member;
import org.sid.entity.PasswordResetToken;
import org.sid.repository.PasswordResetTokenRepository;
import org.sid.service.MemberService;
import org.sid.web.dto.PasswordResetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    @Autowired private MemberService memberService;
    @Autowired private PasswordResetTokenRepository tokenRepository;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
    }

    @GetMapping
    public String displayResetPasswordPage(@RequestParam(required = false) String token,
                                           Model model) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null){
            model.addAttribute("error", "Impossible de trouver le jeton de réinitialisation du mot de passe.");
        } else if (resetToken.isExpired()){
            model.addAttribute("error", "Le jeton a expiré, veuillez demander une nouvelle réinitialisation du mot de passe.");
        } else {
            model.addAttribute("token", resetToken.getToken());
        }

        return "security/reset-password";
    }

    @PostMapping
    @Transactional
    public String handlePasswordReset(@ModelAttribute("passwordResetForm") @Valid PasswordResetDto form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".passwordResetForm", result);
            redirectAttributes.addFlashAttribute("passwordResetForm", form);
            return "redirect:/reset-password?token=" + form.getToken();
        }

        PasswordResetToken token = tokenRepository.findByToken(form.getToken());
        Member member = token.getMember();
        String updatedPassword = passwordEncoder.encode(form.getPassword());
        memberService.updatePassword(updatedPassword, member.getId());
        tokenRepository.delete(token);

        return "redirect:/login?resetSuccess";
    }

}
