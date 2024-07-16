package com.example.testeaxonjava8.commands.aggregates;


import com.example.testeaxonjava8.commonapi.commands.DebitAccountCommand;
import com.example.testeaxonjava8.commonapi.events.AccountDebitEvent;
import com.example.testeaxonjava8.commonapi.exceptions.AmountNegativeException;
import com.example.testeaxonjava8.commonapi.exceptions.BalanceNotSufficientException;
import com.example.testeaxonjava8.commonapi.exceptions.NegativeInitialBalanceexception;
import com.example.testeaxonjava8.commonapi.commands.CreateAccountCommand;
import com.example.testeaxonjava8.commonapi.commands.CreditAccountCommand;
import com.example.testeaxonjava8.commonapi.enums.AccountStatus;
import com.example.testeaxonjava8.commonapi.events.AccountActivatedEvent;
import com.example.testeaxonjava8.commonapi.events.AccountCreatedEvent;
import com.example.testeaxonjava8.commonapi.events.AccountCreditedEvent;
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

    }

    @CommandHandler //si dans cette partie ou onn va stocker les evenement dans event store
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
       //redaction de la logic metier
        if(createAccountCommand.getInitialBalance() < 0) throw new NegativeInitialBalanceexception("the initial balance must be positif !");
       //il faut metter un evenement*
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

   @EventSourcingHandler
    public void on(AccountCreatedEvent event){
            this.accountId= event.getId();
            this.balance=event.getInitialBalance();
            this.currency= event.getCurrency();
            this.status=AccountStatus.CREATED;
            System.out.println("Event persisted: " + event);
            AggregateLifecycle.apply(new AccountActivatedEvent(
                    event.getId(),
                    AccountStatus.ACTIVATED
            ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent accountActivatedEvent){
        this.status=AccountStatus.ACTIVATED;
    }

    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand){
       //la logique metier
        if(creditAccountCommand.getAmount() <0 ) throw new AmountNegativeException("Amount should not be negative!!");
       //mettere event
       AggregateLifecycle.apply(new AccountCreditedEvent(
               creditAccountCommand.getId(),
               creditAccountCommand.getAmount(),
               creditAccountCommand.getCurrenty()
       ));
    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent accountCreditedEvent){
        this.balance+=accountCreditedEvent.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand){
        if(debitAccountCommand.getAmount() <0 ) throw new AmountNegativeException("Amount should not be negative!!");
        if(this.balance < debitAccountCommand.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient !!"+this.balance);

        AggregateLifecycle.apply(new AccountDebitEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrenty()
        ));
    }
    @EventSourcingHandler
    public void on(AccountDebitEvent accountDebitEvent){
        this.balance-=accountDebitEvent.getAmount();
    }

}
