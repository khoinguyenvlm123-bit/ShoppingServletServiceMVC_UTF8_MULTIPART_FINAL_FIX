package vn.iotstar.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class EmailUtil {
    private static final String CONFIG_FILE = "/mail.properties";

    private EmailUtil() {}

    public static void sendOtp(String to, String otp, String purpose) {
        MailConfig cfg = loadConfig();
        if (cfg.username.isBlank() || cfg.password.isBlank()
                || cfg.username.contains("YOUR_GMAIL") || cfg.password.contains("YOUR_APP_PASSWORD")) {
            throw new IllegalStateException(
                    "Chưa cấu hình Gmail gửi OTP. Mở src/main/resources/mail.properties, nhập Gmail và App Password 16 ký tự rồi chạy lại project.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", cfg.host);
        props.put("mail.smtp.port", cfg.port);
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        if ("465".equals(cfg.port)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.trust", cfg.host);
        } else {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.trust", cfg.host);
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(cfg.username, cfg.password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(cfg.from, cfg.fromName, StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            message.setSubject("Mã OTP xác thực - UTE Shopping", StandardCharsets.UTF_8.name());
            message.setContent(buildHtml(otp, purpose), "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            String detail = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            throw new RuntimeException(
                    "Gửi OTP qua Gmail thất bại. Kiểm tra Gmail, App Password và kết nối mạng. Chi tiết: " + detail, e);
        }
    }

    public static boolean isConfigured() {
        MailConfig cfg = loadConfig();
        return !cfg.username.isBlank() && !cfg.password.isBlank()
                && !cfg.username.contains("YOUR_GMAIL") && !cfg.password.contains("YOUR_APP_PASSWORD");
    }

    private static MailConfig loadConfig() {
        Properties fileProps = new Properties();
        try (InputStream in = EmailUtil.class.getResourceAsStream(CONFIG_FILE)) {
            if (in != null) fileProps.load(in);
        } catch (Exception ignored) {
        }

        String username = firstNonBlank(
                System.getProperty("shopping.mail.username"),
                System.getenv("SHOPPING_MAIL_USERNAME"),
                fileProps.getProperty("mail.username"), "");
        String password = firstNonBlank(
                System.getProperty("shopping.mail.password"),
                System.getenv("SHOPPING_MAIL_PASSWORD"),
                fileProps.getProperty("mail.password"), "");
        String from = firstNonBlank(
                System.getProperty("shopping.mail.from"),
                System.getenv("SHOPPING_MAIL_FROM"),
                fileProps.getProperty("mail.from"), username);
        String fromName = firstNonBlank(fileProps.getProperty("mail.fromName"), "UTE Shopping");
        String host = firstNonBlank(fileProps.getProperty("mail.host"), "smtp.gmail.com");
        String port = firstNonBlank(fileProps.getProperty("mail.port"), "587");

        return new MailConfig(username.trim(), password.replace(" ", "").trim(), from.trim(), fromName.trim(), host.trim(), port.trim());
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) return value.trim();
        }
        return "";
    }

    private static String buildHtml(String otp, String purpose) {
        return "<!doctype html><html><body style='margin:0;background:#f4f7fb;font-family:Arial,sans-serif'>"
                + "<div style='max-width:560px;margin:32px auto;background:#fff;border-radius:16px;overflow:hidden;border:1px solid #e5e7eb'>"
                + "<div style='padding:26px 32px;background:#0f6fff;color:#fff'><h2 style='margin:0'>UTE Shopping</h2>"
                + "<p style='margin:8px 0 0;opacity:.9'>Xác thực tài khoản bằng Gmail</p></div>"
                + "<div style='padding:30px 32px;color:#1f2937'><p>Xin chào,</p>"
                + "<p>Mã OTP để <b>" + escapeHtml(purpose) + "</b> của bạn là:</p>"
                + "<div style='margin:24px 0;padding:18px;text-align:center;background:#f3f7ff;border:1px dashed #8ab4ff;border-radius:12px;"
                + "font-size:34px;letter-spacing:10px;font-weight:700;color:#0f6fff'>" + otp + "</div>"
                + "<p>Mã có hiệu lực trong <b>5 phút</b>. Không chia sẻ mã này cho bất kỳ ai.</p>"
                + "<p style='font-size:13px;color:#6b7280'>Nếu bạn không thực hiện yêu cầu này, hãy bỏ qua email.</p></div>"
                + "</div></body></html>";
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private record MailConfig(String username, String password, String from, String fromName, String host, String port) {}
}
