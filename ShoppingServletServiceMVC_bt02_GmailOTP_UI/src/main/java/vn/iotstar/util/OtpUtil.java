package vn.iotstar.util;

import java.security.SecureRandom;

public final class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final long OTP_EXPIRE_MILLIS = 5 * 60 * 1000L;
    public static final long RESEND_COOLDOWN_MILLIS = 60 * 1000L;

    private OtpUtil() {}

    public static String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "email của bạn";
        String[] p = email.split("@", 2);
        String name = p[0];
        String masked;
        if (name.length() <= 2) masked = name.charAt(0) + "***";
        else masked = name.substring(0, 2) + "***" + name.charAt(name.length() - 1);
        return masked + "@" + p[1];
    }
}
