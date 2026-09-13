package BankUtilities;
// نستخدم MessageDigest عشان نحول كلمة المرور إلى Hash
import java.security.MessageDigest;

// نستخدم هذا الـ Exception إذا Java ما لقت خوارزمية الـ Hash المطلوبة
import java.security.NoSuchAlgorithmException;
// نستخدم Base64 عشان نحول نتيجة الـ Hash من bytes إلى نص نقدر نخزنه
import java.util.Base64;

public class PasswordHashingUtility {
    // ميثود تحول كلمة المرور العادية إلى Hash
    public static String hashPassword(String password) {
        try {
            // نحدد خوارزمية SHA-256 اللي بنستخدمها لتحويل كلمة المرور إلى Hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // نحول كلمة المرور إلى بايتات ثم نسوي لها Hash
            //digest.digest(...) اللي يسوي الهاشنق
            //byte[] hashBytes ليش حطيناها بايت لأن نتيجة الهاش تطلع أول شيء على شكل بايتس ،
            // وبعدها بالخطوة الجاية بنحولها إلى سترنق
            byte[] hashBytes = digest.digest(password.getBytes());
            // نحول الـ Hash من bytes إلى نص ونرجعه
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // إذا Java ما لقت خوارزمية SHA-256 نطبع رسالة خطأ
            System.out.println("Error while hashing password.");
            return "";

        }

    }
}
