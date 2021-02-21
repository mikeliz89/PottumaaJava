package Entity;

public class Wallet {
    private int money;
    private int maxMoney;

    public Wallet() {
        init();
    }

    public Wallet(int money) {
        init();
        this.money = money;
    }

    public void init() {
        maxMoney = 1000;
    }

    public int getMoney() {
        return money;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void addMoney(int money) {
        if(this.money + money > this.maxMoney) {
            this.money = this.maxMoney;
            return;
        }
        this.money += money;
    }

    public void reduceMoney(int money) {
        if(this.money - money < 0)
            this.money = 0;
        else
            this.money -= money;
    }

    public void increaseMaxMoney() {
        maxMoney += 100;
    }
}
