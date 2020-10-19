package com.bsoftware.lpbp.security.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GerarSenhaEncodada {
    public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(encoder.encode("chris"));
//        System.out.println(encoder.encode("bela"));
//        System.out.println(encoder.encode("carla"));
//        System.out.println(encoder.encode("daniel"));
//        System.out.println(encoder.encode("elton"));
    }
}
