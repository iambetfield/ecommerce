package com.iternova.ecommerce.configuracion;

import com.iternova.ecommerce.entities.Usuario;
import com.iternova.ecommerce.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImp implements UserDetailsService {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    HttpSession session;

    @Bean
    public BCryptPasswordEncoder bCrypt() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //buscamos el usuario que traemos por parámetro
        Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
        //si existe
        if(optionalUser.isPresent()){
            // asignamos a la session el id del usuario
            session.setAttribute("idusuario", optionalUser.get().getId());
            session.setAttribute("username", optionalUser.get().getNombre());

            //creamos un objeto usuario
            Usuario usuario = optionalUser.get();
            //creamos un objeto del tipo USERDETAIL
            return User.builder()
                    .username(usuario.getNombre())
                    .password(usuario.getPassword())
                    .roles(usuario.getTipo())
                    .build();
        }else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
