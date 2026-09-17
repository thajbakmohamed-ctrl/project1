package BankExceptions;

// Exception خاصة بالحساب غير الفعال
public class InactiveAccountException extends RuntimeException {

    // نستقبل رسالة الخطأ ونرسلها للـException الأساسية
    public InactiveAccountException(String message) {
        super(message);
    }
}