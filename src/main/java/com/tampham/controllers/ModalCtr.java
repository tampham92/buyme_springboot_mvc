package com.tampham.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModalCtr {
    @GetMapping("/modal")
    public String callModal(){

        return "modal/dialog";
    }
}
