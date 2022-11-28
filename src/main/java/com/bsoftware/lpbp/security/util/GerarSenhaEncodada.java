package com.bsoftware.lpbp.security.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class GerarSenhaEncodada {
    public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        System.out.println(encoder.encode("octaniel"));
//        System.out.println(encoder.encode("bela"));
//        System.out.println(encoder.encode("carla"));
//        System.out.println(encoder.encode("daniel"));
//        System.out.println(encoder.encode("elton"));
//        System.out.print("coloca um numero:");
//        Scanner scanner = new Scanner(System.in);
//        int i = scanner.nextInt();
//        int i1 = i + 5;
//        System.out.println(i1);
    }
}
