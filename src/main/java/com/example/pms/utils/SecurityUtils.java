package com.example.pms.utils;

import com.example.pms.account.Account;
import com.example.pms.exception.ApiRequestException;
import com.example.pms.security.authority.Authority;
import com.example.pms.security.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    private SecurityUtils() {

    }

    public static void performAccountAuthorization(String accountUsername) {
        String username = extractUsername();
        if (!username.equals("admin") && !username.equals(accountUsername)) {
            throw new ApiRequestException("You are not authorized to perform this request", HttpStatus.FORBIDDEN);
        }
    }

    public static String extractUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        return currentUser.getUsername();
    }

    public static void performPaymentAuthorization(Account sourceAccount, Account targetAccount) {
        String username = extractUsername();

        if (!username.equals("admin") && (
                !username.equals(sourceAccount.getUser().getUsername()) || !username.equals(targetAccount.getUser().getUsername())
        )) {
            throw new ApiRequestException("You are not authorized to perform this request", HttpStatus.FORBIDDEN);
        }
    }

    public static boolean isRole(User user, String role) {
        for (Authority authority: user.getAuthorities()) {
            if (authority.getAuthorityType().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
