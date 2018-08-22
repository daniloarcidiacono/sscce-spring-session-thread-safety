package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/simple")
public class SimpleController {
    @GetMapping(path = "/whoami")
    public ResponseEntity<String> whoAmI(final Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }
}
