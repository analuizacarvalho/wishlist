package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.AuthenticationInteractor;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.LoginRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationInteractor authenticationInteractor;

    public AuthenticationController(AuthenticationInteractor authenticationInteractor) {
        this.authenticationInteractor = authenticationInteractor;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) {
        String token = authenticationInteractor.login(body.email(), body.password());
        return ResponseEntity.ok().body(new LoginResponse(token));
    }
}
