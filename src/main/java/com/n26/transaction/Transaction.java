package com.n26.transaction;

import com.n26.helper.TimeHelper;
import lombok.*;

@AllArgsConstructor(staticName = "of")
@Getter
public class Transaction {
    private Double amount;
    private Long timestamp;
    private Long currentTimestamp;

    boolean isLastSixtyMinutesTransaction(long currentSeconds){
        return (currentSeconds - timestamp) > 0 && (currentSeconds - timestamp) <= TimeHelper.MAX_MILLISECONDS;
    }
}
