package org.sid.service;

import org.sid.entity.Role;
import org.sid.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleMemberService {

    @Autowired
    private RoleRepository roleRepository;

    public void saveR() {
        if ((roleRepository.findByName("ROLE_PARTICULIER")) != null
                || (roleRepository.findByName("ROLE_PROFESSIONNELLE")) != null
                || (roleRepository.findByName("ROLE_ADMIN")) != null) {

        } else {
            Role d = new Role();
            Role d1 = new Role();
            Role d2 = new Role();
            d.setName("ROLE_PARTICULIER");
            d2.setName("ROLE_PROFESSIONNELLE");
            d1.setName("ROLE_ADMIN");
            int r1 = 1, j = 2;
            while (r1 < j) {

                roleRepository.save(d);
                roleRepository.save(d2);
                roleRepository.save(d1);
                r1 = r1 + 1;
            }
        }
    }
}
