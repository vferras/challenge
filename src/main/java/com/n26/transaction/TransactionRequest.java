package com.n26.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.n26.helper.TimeHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.javatuples.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

import static com.n26.transaction.TransactionState.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
class TransactionRequest {
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("timestamp")
    private String timestamp;

    Pair<TransactionState, Transaction> validateAndMap(TimeHelper timeHelper) throws ParseException {
        Transaction transaction;
        long currentMilliseconds = timeHelper.getCurrentMilliseconds();
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            transaction = Transaction.of(
                    Double.parseDouble(this.amount),
                    dateFormat.parse(this.timestamp).getTime(),
                    currentMilliseconds
            );

            if(transaction.getTimestamp() > currentMilliseconds) {
                return new Pair(NOT_PARSABLE_OR_FUTURE, null);
            }

            if(currentMilliseconds - TimeHelper.MAX_MILLISECONDS >= transaction.getTimestamp()) {
                return new Pair(OLD, null);
            }
        }
        catch (NumberFormatException exception) {
            return new Pair(NOT_PARSABLE_OR_FUTURE, null);
        }

        return new Pair(CREATED, transaction);
    }
}
