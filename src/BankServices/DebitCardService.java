package BankServices;
// نستخدم DebitCard عشان نخزن بطاقات العملاء
import BankModels.DebitCard;
// نستخدم ArrayList عشان نخزن أكثر من بطاقة
import java.util.ArrayList;
// نستخدم Optional لأن البحث عن البطاقة ممكن يلقى نتيجة أو لا
import java.util.Optional;
// Service مسؤول عن إدارة بطاقات الخصم
public class DebitCardService {
    // قائمة نخزن فيها كل بطاقات الخصم
    private ArrayList<DebitCard> debitCards;
    // Constructor ينشئ قائمة فاضية للبطاقات
    public DebitCardService() {
        // ننشئ ArrayList فاضية
        debitCards = new ArrayList<>();
    }
    // نضيف بطاقة جديدة إلى النظام
    public void addDebitCard(DebitCard debitCard) {
        // نضيف البطاقة إلى القائمة
        debitCards.add(debitCard);
    }
    // نبحث عن البطاقة باستخدام رقم الحساب
    public Optional<DebitCard> findCardByAccountId(String accountId) {
        // نمر على كل البطاقات ونبحث عن البطاقة المرتبطة بالحساب
        return debitCards.stream()
                // نختار البطاقة اللي رقم الحساب مالها يساوي الرقم المطلوب
                .filter(card -> card.getAccountId().equals(accountId))
                // نرجع أول بطاقة مطابقة
                .findFirst();
    }
    // نرجع كل البطاقات الموجودة في النظام
    public ArrayList<DebitCard> getAllDebitCards() {
        // نرجع قائمة البطاقات
        return debitCards;
    }
    // نرجع الحد المسموح للسحب حسب نوع البطاقة
    public double getWithdrawLimit(String cardType) {

        // إذا نوع البطاقة Mastercard
        if (cardType.equalsIgnoreCase("MASTERCARD")) {
            // الحد اليومي للسحب هو 5000
            return 5000;
            // إذا نوع البطاقة Titanium
        } else if (cardType.equalsIgnoreCase("TITANIUM")) {
            // الحد اليومي للسحب هو 10000
            return 10000;
            // إذا نوع البطاقة Platinum
        } else if (cardType.equalsIgnoreCase("PLATINUM")) {
            // الحد اليومي للسحب هو 20000
            return 20000;
        }

        // إذا نوع البطاقة غير معروف نرجع صفر
        return 0;
    }
    // نرجع الحد المسموح للتحويل حسب نوع البطاقة
    public double getTransferLimit(String cardType) {

        // إذا نوع البطاقة Mastercard
        if (cardType.equalsIgnoreCase("MASTERCARD")) {
            // الحد اليومي للتحويل هو 10000
            return 10000;
            // إذا نوع البطاقة Titanium
        } else if (cardType.equalsIgnoreCase("TITANIUM")) {
            // الحد اليومي للتحويل هو 20000
            return 20000;
            // إذا نوع البطاقة Platinum
        } else if (cardType.equalsIgnoreCase("PLATINUM")) {
            // الحد اليومي للتحويل هو 40000
            return 40000;
        }

        // إذا نوع البطاقة غير معروف نرجع صفر
        return 0;
    }
}