package BankServices;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitCardServiceTest {

    @Test
    public void mastercardWithdrawTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                5000,
                service.getWithdrawLimit("MASTERCARD")
        );
    }

    @Test
    public void titaniumWithdrawTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                10000,
                service.getWithdrawLimit("TITANIUM")
        );
    }

    @Test
    public void platinumWithdrawTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                20000,
                service.getWithdrawLimit("PLATINUM")
        );
    }

    @Test
    public void mastercardTransferTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                10000,
                service.getTransferLimit("MASTERCARD")
        );
    }

    @Test
    public void mastercardOwnTransferTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                20000,
                service.getOwnTransferLimit("MASTERCARD")
        );
    }

    @Test
    public void depositLimitTest() {

        DebitCardService service = new DebitCardService();

        assertEquals(
                200000,
                service.getOwnDepositLimit("MASTERCARD")
        );
    }
}