package ma.enset.accountcqrses.commonapi.dtos;

import lombok.Data;

@Data
public class DebitAccountRequestDto {
    private String accountId;
    private double amount;
    private String currency;
}
