package com.tampham.services;

import com.tampham.models.User;

public interface EmailService {
    boolean sendVerifyCode(User user);
}
