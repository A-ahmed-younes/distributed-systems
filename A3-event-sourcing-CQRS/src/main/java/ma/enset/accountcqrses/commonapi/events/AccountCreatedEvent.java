package ma.enset.accountcqrses.commonapi.events;

import lombok.Getter;
import ma.enset.accountcqrses.commonapi.enums.AccountStatus;

public class AccountCreatedEvent extends BaseEvent<String> {
    @Getter
    public String currency;
    @Getter
    public double balance;
    @Getter

    private AccountStatus status;

    public AccountCreatedEvent(String id, String currency, double balance, AccountStatus status) {
        super(id);
        this.currency = currency;
        this.balance = balance;
        this.status = status;
    }
}
