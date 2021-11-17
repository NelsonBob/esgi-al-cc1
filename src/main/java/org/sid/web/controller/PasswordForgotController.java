package org.sid.web.controller;

import org.sid.entity.Mail;
import org.sid.entity.Member;
import org.sid.entity.PasswordResetToken;
import org.sid.repository.PasswordResetTokenRepository;
import org.sid.service.EmailService;
import org.sid.service.MemberService;
import org.sid.web.dto.PasswordForgotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/forgot-password")
public class PasswordForgotController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;

    @ModelAttribute("forgotPasswordForm")
    public PasswordForgotDto forgotPasswordDto() {
        return new PasswordForgotDto();
    }

    @GetMapping
    public String displayForgotPasswordPage() {
        return "security/forgot-password";
    }

    @PostMapping
    public String processForgotPasswordForm(@ModelAttribute("forgotPasswordForm") @Valid PasswordForgotDto form,
                                            BindingResult result,
                                            HttpServletRequest request) {

        if (result.hasErrors()) {
            return "security/forgot-password";
        }

        Member member = memberService.findByEmail(form.getEmail());
        if (member == null) {
            result.rejectValue("email", null, "Nous n'avons pas pu trouver de compte pour cette adresse e-mail.");
            return "security/forgot-password";
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setMember(member);
        token.setExpiryDate(30);
        tokenRepository.save(token);

        Mail mail = new Mail();
        mail.setFrom("ESGI AL CC1 <koumwinnie@zohomail.com>");
        mail.setTo(member.getEmail());
        mail.setSubject("Demande de réinitialisation du mot de passe");
        mail.setText("\n"
                + "Pour terminer le processus de réinitialisation du mot de passe," + "\n" + " veuillez cliquer ici:"
                + "http://localhost:8180/reset-password?token=" + token.getToken());

        emailService.sendEmail(mail);

        return "redirect:/forgot-password?success";

    }

}
