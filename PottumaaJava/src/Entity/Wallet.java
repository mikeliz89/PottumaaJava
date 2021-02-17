package Entity;

public class Wallet {
    private int money;
    private int maxMoney;

    public Wallet(int money) {
        this.money = money;
        maxMoney = 1000;
    }

    public int GetMoneyAmount() {
        return money;
    }

    public int GetMaxMoneyAmount() {
        return maxMoney;
    }

    public void AddMoney(int amount) {
        if(this.money + amount > this.maxMoney) {
            this.money = this.maxMoney;
            return;
        }
        this.money += amount;
    }

    public void TakeMoney(int amount) {
        if(this.money - amount < 0)
            this.money = 0;
        else
            this.money -= amount;
    }

    public void IncreaseMax() {
        maxMoney += 100;
    }
}
