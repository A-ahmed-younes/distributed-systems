package ma.enset.accountcqrses.commands.aggregate;

import ma.enset.accountcqrses.commonapi.commands.CreateAccountCommand;
import ma.enset.accountcqrses.commonapi.commands.CreditAccountCommand;
import ma.enset.accountcqrses.commonapi.commands.DebitAccountCommand;
import ma.enset.accountcqrses.commonapi.dtos.InsufficientBalanceException;
import ma.enset.accountcqrses.commonapi.enums.AccountStatus;
import ma.enset.accountcqrses.commonapi.events.AccountActivatedEvent;
import ma.enset.accountcqrses.commonapi.events.AccountCreatedEvent;
import ma.enset.accountcqrses.commonapi.events.AccountCreditedEvent;
import ma.enset.accountcqrses.commonapi.events.AccountDebitedEvent;
import ma.enset.accountcqrses.commonapi.exceptions.AmountNegativeException;
import ma.enset.accountcqrses.commonapi.exceptions.NegativeBalanceException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;


@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;

    private AccountStatus status;

    public AccountAggregate() {
        // Required by Axon to build a default Aggregate prior to Event Sourcing
    }

    // Decision
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        if (command.getInitialBalance() < 0) throw new NegativeBalanceException("Initial balance cannot be negative");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getCurrency(),
                command.getInitialBalance(),
                AccountStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        if (command.getAmount() < 0) throw new AmountNegativeException("Cannot credit a negative amount");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        if (command.getAmount() < 0) throw new AmountNegativeException("Cannot credit a negative amount");
        if (this.balance - command.getAmount() < 0)
            throw new InsufficientBalanceException("Insufficient balance " + balance);
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getCurrency(),
                command.getAmount()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }


}
