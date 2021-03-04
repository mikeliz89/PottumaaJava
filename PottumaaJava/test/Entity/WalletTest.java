package Entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    Wallet myWallet;
    private final int _moneyInWallet = 500;

    public WalletTest() {
        int money = _moneyInWallet;
        myWallet = new Wallet(money);
    }

    @Test
    void getMaxMoney() {
        var maxMoney = myWallet.getMaxMoney();
        assertEquals(1000, maxMoney);
    }

    @Test
    void getMoney() {
        var money = myWallet.getMoney();
        assertEquals(500, money);
    }

    @Test
    void getMaxMoney_IncreaseMaxShouldReturnMaxPlus100() {
        var maxMoney =  myWallet.getMaxMoney();
        myWallet.increaseMaxMoney();
        var newMaxMoney = myWallet.getMaxMoney();
        assertTrue(newMaxMoney == maxMoney+100);
        myWallet.increaseMaxMoney();
        newMaxMoney = myWallet.getMaxMoney();
        assertTrue(newMaxMoney == maxMoney+200);
    }

    @Test
    void getMoney_Taking100MoneyFromWalletShouldRemove100FromWallet() {
        myWallet.reduceMoney(100);
        var newMoney = myWallet.getMoney();
        assertEquals(400, newMoney);
    }

    @Test
    void getMoney_TakeMoney100ShouldRemove100FromWallet() {
        myWallet.reduceMoney(40);
        var newMoney = myWallet.getMoney();
        assertEquals(460, newMoney);
    }

    @Test
    void getMoney_TryingToTakeMoreFromWalletThanThereActuallyIs_WalletShouldHaveZeroMoney() {
        myWallet.reduceMoney(myWallet.getMoney() + 10);
        var moneyInWallet = myWallet.getMoney();
        assertEquals(0, moneyInWallet);
    }

    @Test
    void getMoney_AddMoneyToWalletShouldWork() {
        var moneyInWalletAtTheStart = myWallet.getMoney();
        myWallet.addMoney(100);
        var moneyInWalletAfterAdding = myWallet.getMoney();
        assertEquals(moneyInWalletAfterAdding, moneyInWalletAtTheStart + 100);
    }

    @Test
    void getMoney_AddMoreManyToWalletThanItsMaxAmountIs_WalletShouldHaveMaxAmountOfMoney() {
        myWallet.addMoney(1500);
        var moneyInWalletAfterAdding = myWallet.getMoney();
        assertEquals(moneyInWalletAfterAdding, myWallet.getMaxMoney());
    }

}