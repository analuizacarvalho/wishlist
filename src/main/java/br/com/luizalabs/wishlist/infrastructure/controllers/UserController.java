package br.com.luizalabs.wishlist.infrastructure.controllers;

import br.com.luizalabs.wishlist.application.usecases.AuthenticationInteractor;
import br.com.luizalabs.wishlist.application.usecases.UserInteractor;
import br.com.luizalabs.wishlist.domain.entity.User;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterRequest;
import br.com.luizalabs.wishlist.infrastructure.controllers.dto.RegisterResponse;
import br.com.luizalabs.wishlist.infrastructure.controllers.mapper.UserDTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserInteractor userInteractor;
    private final AuthenticationInteractor authenticationInteractor;
    private final UserDTOMapper userDTOMapper;

    public UserController(UserInteractor userInteractor,
                          AuthenticationInteractor authenticationInteractor,
                          UserDTOMapper userDTOMapper) {
        this.userInteractor = userInteractor;
        this.authenticationInteractor = authenticationInteractor;
        this.userDTOMapper = userDTOMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        User user = userDTOMapper.toUser(registerRequest);
        User savedUser = userInteractor.createUser(user);
        String token = authenticationInteractor.login(registerRequest.email(), registerRequest.password());
        return ResponseEntity.ok().body(userDTOMapper.toRegisterResponse(savedUser, token));
    }
}
