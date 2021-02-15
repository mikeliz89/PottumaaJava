package Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    Wallet myWallet;

    public WalletTest() {
        int money = 500;
        myWallet = new Wallet(money);
    }

    @Test
    void getMaxMoneyAmount() {
        var maxMoneyAmount = myWallet.GetMaxMoneyAmount();
        assertEquals(1000, maxMoneyAmount);
    }

    @Test
    void getMoneyAmount() {
        var moneyAmount = myWallet.GetMoneyAmount();
        assertEquals(500, moneyAmount);
    }

    @Test
    void IncreaseMaxShouldReturnMaxPlus100() {
        var maxMoneyAmount =  myWallet.GetMaxMoneyAmount();
        myWallet.IncreaseMax();
        var newMaxAmount = myWallet.GetMaxMoneyAmount();
        assertTrue(newMaxAmount == maxMoneyAmount+100);
        myWallet.IncreaseMax();
        newMaxAmount = myWallet.GetMaxMoneyAmount();
        assertTrue(newMaxAmount == maxMoneyAmount+200);
    }

    @Test
    void Taking100MoneyFromWalletShouldRemove100FromWallet() {
        myWallet.TakeMoney(100);
        var newMoney = myWallet.GetMoneyAmount();
        assertEquals(400, newMoney);
    }

    @Test
    void TakeMoney100ShouldRemove100FromWallet() {
        myWallet.TakeMoney(40);
        var newMoney = myWallet.GetMoneyAmount();
        assertEquals(460, newMoney);
    }

    @Test
    void TryingToTakeMoreFromWalletThanThereActuallyIs_WalletShouldHaveZeroMoney() {
        myWallet.TakeMoney(myWallet.GetMoneyAmount() + 10);
        var moneyInWallet = myWallet.GetMoneyAmount();
        assertEquals(0, moneyInWallet);
    }

}