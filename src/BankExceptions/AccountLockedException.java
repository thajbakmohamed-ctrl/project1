package BankExceptions;

// Exception خاصة بقفل الحساب بعد محاولات تسجيل دخول فاشلة
public class AccountLockedException extends RuntimeException {

    // نستقبل رسالة الخطأ ونرسلها للـException الأساسية
    public AccountLockedException(String message) {
        super(message);
    }
}
