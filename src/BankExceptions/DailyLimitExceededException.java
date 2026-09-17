package BankExceptions;

// Exception خاصة بتجاوز الحد اليومي للبطاقة
public class DailyLimitExceededException extends RuntimeException {

    // نستقبل رسالة الخطأ ونرسلها للـException الأساسية
    public DailyLimitExceededException(String message) {
        super(message);
    }
}