package ma.enset.accountcqrses.commonapi.events;

import lombok.Getter;

public class AccountCreditedEvent extends BaseEvent<String> {
    @Getter
    public String currency;
    @Getter
    public double amount;


    public AccountCreditedEvent(String id, String currency, double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
