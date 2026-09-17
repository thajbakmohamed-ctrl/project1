package BankModels;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {

    @Test
    public void createAccountTest() {

        Account account =
                new Account("CHK01", "CUS01", "CHECKING", 1000);

        assertEquals("CHK01", account.getAccountId());
        assertEquals("CUS01", account.getCustomerId());
        assertEquals("CHECKING", account.getAccountType());
        assertEquals(1000, account.getBalance());
        assertTrue(account.isActive());
        assertEquals(0, account.getOverdraftCount());
    }
}