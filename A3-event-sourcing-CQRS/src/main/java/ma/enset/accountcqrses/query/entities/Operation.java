package ma.enset.accountcqrses.query.entities;

import ma.enset.accountcqrses.commonapi.enums.OperationType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @ManyToOne
    private Account account;

}
