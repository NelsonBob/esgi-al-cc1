package org.sid.esgialcc1;

import org.sid.service.MemberService;
import org.sid.service.RoleMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@ComponentScan({ "org.sid.config", "org.sid.service", "org.sid.web.controller", "org.sid.web.dto" })
@EntityScan("org.sid.entity")
@EnableJpaRepositories("org.sid.repository")
public class EsgiAlCc1Application implements CommandLineRunner {

	@Autowired
	private RoleMemberService role;

	@Autowired
	private MemberService memberService;

	public static void main(String[] args) {
		SpringApplication.run(EsgiAlCc1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		role.saveR();
		memberService.saveAdminFist();
	}
}
