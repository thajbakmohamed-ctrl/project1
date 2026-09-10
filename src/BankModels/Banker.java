package BankModels;

public class Banker extends User{
    private String bankerId;
    //GETTERS
    public String getBankerId() {
        return bankerId;
    }

    // اذا ابي اسوي بنكر لازم اعطيه هالاشياء للعلم ماحطينتا رول لان هو بنكر اساسا
    //ضفنا البنكر ايدي عشان من اليوزر ايدي بيعرف ان البنكر
    //userId يعرّف الشخص كمستخدم في النظام، وbankerId
    // يعرّفه كموظف Banker، أما "BANKER" فهي اللي تحدد الـrole
    public Banker(String bankerId,String userId, String name,
                  String email, String phone,
                  String passwordHash) {
        //  ضفنا بنكر مع " عشان نقول ان هو اصلا بنكر
        //
        //  السوبر معنناه روح حق البارنت اللي هو اليوزر و شغل الكونستركتر ماله
        super(userId, name, email, phone, passwordHash,"BANKER");
        //يعني نخزن الـbankerId الخاص بالـBanker نفسه
        this.bankerId = bankerId;
    }
}
