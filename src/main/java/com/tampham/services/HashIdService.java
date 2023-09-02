package com.tampham.services;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HashIdService {
    @Value("${spring.app.secret}")
    private String salt;

    private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    public String endCodeId(Long id){
        Hashids hashids = new Hashids(salt, 6, ALPHABET);
        return hashids.encode(id);
    }

    public Long deCodeId(String id){
        Hashids hashids = new Hashids(salt, 6, ALPHABET);
        long[] result = hashids.decode(id);;

        if (result.length > 0){
            return result[0];
        }
        return null;
    }
}

