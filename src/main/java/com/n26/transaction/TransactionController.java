package com.n26.transaction;

import com.n26.helper.TimeHelper;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class TransactionController {

    private TransactionService transactionService;
    private TimeHelper timeHelper;

    TransactionController(TransactionService transactionService, TimeHelper timeHelper) {
        this.transactionService = transactionService;
        this.timeHelper = timeHelper;
    }

    @PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity addTransaction(@RequestBody TransactionRequest request) throws ParseException {
        try {
            Pair<TransactionState, Transaction> transactionValidation = request.validateAndMap(timeHelper);

            switch (transactionValidation.getValue0()) {
                case CREATED:
                    Transaction transaction = transactionValidation.getValue1();
                    transactionService.add(transaction);

                    return new ResponseEntity(HttpStatus.CREATED);
                case NOT_PARSABLE_OR_FUTURE:
                    return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
                case OLD:
                    return new ResponseEntity(HttpStatus.NO_CONTENT);
                default:
                    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (ParseException exception) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping(value = "/transactions")
    public ResponseEntity deleteTransactions() {
        transactionService.deleteAll();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
