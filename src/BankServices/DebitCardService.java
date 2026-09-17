package BankServices;
// نستخدم DebitCard عشان نخزن بطاقات العملاء
import BankModels.DebitCard;
// نستخدم Account عشان نربط البطاقة بالحساب
import BankModels.Account;
// نستخدم ArrayList عشان نخزن أكثر من بطاقة
import java.util.ArrayList;
// نستخدم Optional لأن البحث عن البطاقة ممكن يلقى نتيجة أو لا
import java.util.Optional;
// Service مسؤول عن إدارة بطاقات الخصم
// نستخدم LocalDate عشان نعرف تاريخ اليوم
import java.time.LocalDate;
import BankExceptions.DailyLimitExceededException;
public class DebitCardService {
    // قائمة نخزن فيها كل بطاقات الخصم
    private ArrayList<DebitCard> debitCards;

    // Constructor ينشئ قائمة فاضية للبطاقات
    public DebitCardService() {

        // ننشئ ArrayList فاضية
        debitCards = new ArrayList<>();
    }
    // ننشئ بطاقة Mastercard افتراضية لأي حساب جديد
    public DebitCard createDefaultCard(Account account) {
        // نسوي Card ID باستخدام رقم الحساب عشان يكون مميز
        String cardId = "CARD-" + account.getAccountId();
        // نسوي رقم بطاقة باستخدام الوقت الحالي
        String cardNumber = String.valueOf(System.currentTimeMillis());
        // نخلي نوع البطاقة الافتراضية Mastercard
        String cardType = "MASTERCARD";
        // نحط تاريخ انتهاء افتراضي للبطاقة
        String expiryDate = "12/31";
        // ننشئ بطاقة جديدة ونربطها بالحساب
        DebitCard debitCard = new DebitCard(cardId, account.getAccountId(), cardNumber,
                        cardType, expiryDate);

        // نضيف البطاقة إلى قائمة البطاقات داخل النظام
        addDebitCard(debitCard);
        // نرجع البطاقة عشان نقدر نحفظها بعدين
        return debitCard;
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
            // نرجع حد السحب
            return 5000;
            // إذا نوع البطاقة Titanium
        } else if (cardType.equalsIgnoreCase("TITANIUM")) {
            // نرجع حد السحب
            return 10000;
            // إذا نوع البطاقة Platinum
        } else if (cardType.equalsIgnoreCase("PLATINUM")) {
            // نرجع حد السحب
            return 20000;
        }
        // إذا نوع البطاقة غير معروف نرجع صفر
        return 0;
    }
    // نرجع الحد المسموح للتحويل حسب نوع البطاقة
    public double getTransferLimit(String cardType) {
        // إذا نوع البطاقة Mastercard
        if (cardType.equalsIgnoreCase("MASTERCARD")) {
            // نرجع حد التحويل
            return 10000;
            // إذا نوع البطاقة Titanium
        } else if (cardType.equalsIgnoreCase("TITANIUM")) {
            // نرجع حد التحويل
            return 20000;
            // إذا نوع البطاقة Platinum
        } else if (cardType.equalsIgnoreCase("PLATINUM")) {
            // نرجع حد التحويل
            return 40000;
        }
        // إذا نوع البطاقة غير معروف نرجع صفر
        return 0;
    }
    // نجيب حد التحويل اليومي بين حسابات نفس العميل
    public double getOwnTransferLimit(String cardType) {

        // حد Mastercard للتحويل بين حسابات العميل هو 20000
        if (cardType.equalsIgnoreCase("MASTERCARD")) {
            return 20000;

            // حد Titanium للتحويل بين حسابات العميل هو 40000
        } else if (cardType.equalsIgnoreCase("TITANIUM")) {
            return 40000;

            // حد Platinum للتحويل بين حسابات العميل هو 80000
        } else if (cardType.equalsIgnoreCase("PLATINUM")) {
            return 80000;
        }

        // إذا نوع البطاقة غير معروف
        return 0;
    }


    // نجيب حد الإيداع اليومي
    public double getDepositLimit(String cardType) {
        // كل أنواع البطاقات لها نفس حد الإيداع اليومي
        return 100000;
    }


    // نجيب حد الإيداع اليومي في حساب العميل نفسه
    public double getOwnDepositLimit(String cardType) {
        // كل أنواع البطاقات لها نفس حد الإيداع في الحساب الشخصي
        return 200000;
    }


    // نتأكد إن استخدام البطاقة محسوب لليوم الحالي
    public void resetDailyUsageIfNeeded(DebitCard card) {
        // نجيب تاريخ اليوم
        String today = LocalDate.now().toString();
        // إذا ما عندنا تاريخ محفوظ أو التاريخ المحفوظ مو اليوم
        if (card.getDailyUsageDate() == null || !card.getDailyUsageDate().equals(today)) {
            // نصفر مجموع السحب اليومي
            card.setDailyWithdrawUsed(0);
            // نصفر مجموع التحويل العادي اليومي
            card.setDailyTransferUsed(0);
            // نصفر مجموع التحويل بين حسابات نفس العميل
            card.setDailyOwnTransferUsed(0);
            // نصفر مجموع الإيداع اليومي
            card.setDailyDepositUsed(0);
            // نخزن تاريخ اليوم
            card.setDailyUsageDate(today);
        }
    }
    // نتأكد إن عملية السحب ما تتجاوز الحد اليومي للبطاقة
    public void checkWithdrawDailyLimit(DebitCard card, double amount) {
        // نصفر الاستخدام إذا دخل يوم جديد
        resetDailyUsageIfNeeded(card);
        // نجيب حد السحب حسب نوع البطاقة
        double withdrawLimit = getWithdrawLimit(card.getCardType());
        // نحسب شكثر بيصير مجموع السحب بعد العملية
        double totalWithdraw = card.getDailyWithdrawUsed() + amount;
        // إذا المجموع تجاوز الحد اليومي نرمي Exception
        if (totalWithdraw > withdrawLimit) {
            throw new DailyLimitExceededException("Daily withdraw limit exceeded.");
        }
    }
}