package user.saulo;

public class Account {
    private String name;
    private String description;
    private double balance;
    private double debt;
    private double credit;
    private double goal;


    public Account(String name, String description) {
        this.name = name;
        this.description = description;
        this.debt = 0;
        this.balance = 0;
        this.credit = 0;
        this.goal = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }
}
