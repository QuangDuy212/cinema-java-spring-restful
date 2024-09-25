package com.vn.cinema_internal_java_spring_rest.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Permission;
import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.repository.PermissionRepository;
import com.vn.cinema_internal_java_spring_rest.repository.RoleRepository;
import com.vn.cinema_internal_java_spring_rest.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a category", "/api/v1/categories", "POST", "CATEGORIES"));
            arr.add(new Permission("Update a category", "/api/v1/categories", "PUT", "CATEGORIES"));
            arr.add(new Permission("Delete a category", "/api/v1/categories/{id}", "DELETE", "CATEGORIES"));
            arr.add(new Permission("Get a category by id", "/api/v1/categories/{id}", "GET", "CATEGORIES"));
            arr.add(new Permission("Get categories with pagination", "/api/v1/categories", "GET", "CATEGORIES"));

            arr.add(new Permission("Create a show", "/api/v1/shows", "POST", "SHOWS"));
            arr.add(new Permission("Update a show", "/api/v1/shows", "PUT", "SHOWS"));
            arr.add(new Permission("Delete a show", "/api/v1/shows/{id}", "DELETE", "SHOWS"));
            arr.add(new Permission("Get a show by id", "/api/v1/shows/{id}", "GET", "SHOWS"));
            arr.add(new Permission("Get shows with pagination", "/api/v1/shows", "GET", "SHOWS"));

            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            arr.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            arr.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            arr.add(new Permission("Delete a resume", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            arr.add(new Permission("Get a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            arr.add(new Permission("Get resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));

            arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));

            arr.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            arr.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE", "USERS"));
            arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET", "USERS"));

            arr.add(new Permission("Create a seat", "/api/v1/seats", "POST", "SEATS"));
            arr.add(new Permission("Update a seat", "/api/v1/seats", "PUT", "SEATS"));
            arr.add(new Permission("Delete a seat", "/api/v1/seats/{id}", "DELETE", "SEATS"));
            arr.add(new Permission("Get a seat by id", "/api/v1/seats/{id}", "GET", "SEATS"));
            arr.add(new Permission("Get seats with pagination", "/api/v1/seats", "GET", "SEATS"));

            arr.add(new Permission("Create a bill", "/api/v1/bills", "POST", "BILLS"));
            arr.add(new Permission("Update a bill", "/api/v1/bills", "PUT", "BILLS"));
            arr.add(new Permission("Delete a bill", "/api/v1/bills/{id}", "DELETE", "BILLS"));
            arr.add(new Permission("Get a bill by id", "/api/v1/bills/{id}", "GET", "BILLS"));
            arr.add(new Permission("Get bills with pagination", "/api/v1/bills", "GET", "BILLS"));

            arr.add(new Permission("Create a time", "/api/v1/times", "POST", "TIMES"));
            arr.add(new Permission("Update a time", "/api/v1/times", "PUT", "TIMES"));
            arr.add(new Permission("Delete a time", "/api/v1/times/{id}", "DELETE", "TIMES"));
            arr.add(new Permission("Get a time by id", "/api/v1/times/{id}", "GET", "TIMES"));
            arr.add(new Permission("Get times with pagination", "/api/v1/times", "GET", "TIMES"));

            arr.add(new Permission("Create a history", "/api/v1/histories", "POST", "HISTORIES"));
            arr.add(new Permission("Update a history", "/api/v1/histories", "PUT", "HISTORIES"));
            arr.add(new Permission("Delete a history", "/api/v1/histories/{id}", "DELETE", "HISTORIES"));
            arr.add(new Permission("Get a history by id", "/api/v1/histories/{id}", "GET", "HISTORIES"));
            arr.add(new Permission("Get histories with pagination", "/api/v1/histories", "GET", "HISTORIES"));

            arr.add(new Permission("Download a file", "/api/v1/files", "POST", "FILES"));
            arr.add(new Permission("Upload a file", "/api/v1/files", "GET", "FILES"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setActive(true);
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAddress("hn");
            adminUser.setFullName("Superadmin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            adminUser.setPhone("123456789");
            adminUser.setActive(true);

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }

}
