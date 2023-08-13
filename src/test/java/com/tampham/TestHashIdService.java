package com.tampham;

import com.tampham.services.HashIdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestHashIdService {

//    @TestConfiguration
//    public static class HashIdServiceTestConfig{
//        @Bean
//        HashIdService hashIdService(){
//            return new  HashIdService();
//        }
//    }

    @InjectMocks
    HashIdService hashIdService;

    @Test
    public void hashIdServiceTestEncoded(){
        long id = 1;
        String result = hashIdService.endCodeId(id);
        assertEquals("09EJ3A", result);
    }

    @Test
    public void hashIdServiceTestDecoded(){
        long id = 1;
        long result = hashIdService.deCodeId("09EJ3A");
        assertEquals(id, result);
    }
}
