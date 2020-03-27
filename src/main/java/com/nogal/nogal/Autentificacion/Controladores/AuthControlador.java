package com.nogal.nogal.Autentificacion.Controladores;

import com.nogal.nogal.Autentificacion.Modelos.AuthenticationRequest;
import com.nogal.nogal.Autentificacion.Modelos.AuthenticationResponse;
import com.nogal.nogal.Autentificacion.Modelos.User;
import com.nogal.nogal.Autentificacion.Servicios.MyUserDetailsServicio;
import com.nogal.nogal.Autentificacion.Servicios.UserServicio;
import com.nogal.nogal.Autentificacion.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class AuthControlador{

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private MyUserDetailsServicio userServicio;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserServicio servicio;

    @PostMapping("/autentificar")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest request) throws Exception{
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Usuario o contraseña incorrecto", e);
        } 
        final UserDetails userDetails = userServicio.loadUserByUsername(request.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/crear")
    public long crearUsuario(@RequestBody User request){
        return servicio.crear(request);
    }

    @GetMapping("/obtenerID/{token}")
    public long obtenerID(@PathVariable("token") String token){
        long ID = jwtTokenUtil.extractId(token);
        return ID;
    }

    @GetMapping("/obtenerUser/{id}")
    public String obtenerUser(@PathVariable("id") long id){
        return servicio.obtenerUser(id);
    }

    @GetMapping("/obtenerRol/{token}")
    public String obtenerRol(@PathVariable("token") String token){
        long ID = jwtTokenUtil.extractId(token);
        return servicio.obtenerRol(ID);
    }

}