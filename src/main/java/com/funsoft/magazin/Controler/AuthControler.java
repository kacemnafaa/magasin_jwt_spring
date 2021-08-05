package com.funsoft.magazin.Controler;

import com.funsoft.magazin.Repository.RoleRepository;
import com.funsoft.magazin.Repository.UserRepository;
import com.funsoft.magazin.Securité.JWT.JwtUtils;
import com.funsoft.magazin.Securité.Service.UserDetailsImpl;
import com.funsoft.magazin.iomodel.Request.LoginRequest;
import com.funsoft.magazin.iomodel.Request.Respense.JWTRespence;
import com.funsoft.magazin.iomodel.Request.Respense.MessageResponse;
import com.funsoft.magazin.iomodel.Request.SignupRequest;
import com.funsoft.magazin.model.Erole;
import com.funsoft.magazin.model.Role;
import com.funsoft.magazin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")//locolhost:8082/magasin/api/auth/...(authorisé)



public class AuthControler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping("/signup")
    public ResponseEntity<?> subscribe(@Valid @RequestBody SignupRequest signupRequest){
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error : Username is already taken !!!"));
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error : Email is already taken !!!") );
        }
        // create new user's account
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        // créer la liste des ROLE
        Set<String> strroles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if(strroles == null){
            Role roleuser = roleRepository.findByName(Erole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error : role is not found"));
            roles.add(roleuser);
        } else {
            strroles.forEach(role ->
                    {
                        switch(role){
                            case "admin":
                                Role roleadmin = roleRepository.findByName(Erole.ROLE_ADMIN)
                                        .orElseThrow(()-> new RuntimeException("Error : role is not found"));
                                roles.add(roleadmin);
                                break;
                            case "manager":
                                Role rolemanager = roleRepository.findByName(Erole.ROLE_MANAGER)
                                        .orElseThrow(() -> new RuntimeException("Error : role is not found"));
                                roles.add(rolemanager);
                                break;
                            case "secretaire" :
                                Role rolesec = roleRepository.findByName(Erole.ROLE_MANAGER)
                                        .orElseThrow(() -> new RuntimeException("Error : role is not found"));
                                roles.add(rolesec);
                                break;
                            default:
                                Role roleuser = roleRepository.findByName(Erole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error : role is not found"));
                                roles.add(roleuser);
                        }
                    }
            );
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registred successfully !!!"));
    }
    @PostMapping(value="/signin")
    public ResponseEntity<?> authentification(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // personnaliser le contexte de spring security (pour controler les droits)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // générer le token
        String jwt = jwtUtils.generateJwtToken(authentication);
        //
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        // récupérer la liste des roles de l'utilisateur
        List<String> roles = user.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(
                new JWTRespence(jwt,
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(), roles)
        );
    }


}
