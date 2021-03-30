package io.github.chait.transaction.controller;

import io.github.chait.transaction.dto.InsightPreferenceDTO;
import io.github.chait.transaction.dto.InsightResponseDTO;
import io.github.chait.transaction.dto.TransactionDTO;
import io.github.chait.transaction.dto.TransactionFlow;
import io.github.chait.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionRestController {

    @Autowired
    TransactionService transactionService;

    /*@PostMapping("/create")
    public void createTransaction(@RequestBody TransactionDTO dto) {
        this.transactionService.putTransaction(dto);
    }*/

    @PostMapping("/create/es")
    public void createTransactionInElastic(@RequestBody TransactionDTO dto) {
        this.transactionService.putTransactionEs(dto);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionByAccount(@PathVariable("accountId") String accountId) {
        return ResponseEntity.ok(transactionService.getTransactionByAccountId(accountId));
    }

    @GetMapping("/{txType}")
    public ResponseEntity<Page<TransactionDTO>> getTransactionByType(@PathVariable("txType")String transactionType) {
        return ResponseEntity.ok(transactionService.getTransactionByTransactionType(transactionType));
    }

    @GetMapping("/flows/{accountId}/{txType}")
    public ResponseEntity<List<TransactionFlow>> getIncomeFlows(@PathVariable("accountId") String accountId, @PathVariable("txType") String txType) {
        return ResponseEntity.ok(transactionService.getTransactionFlowByAccountIdAndTransactionType(accountId, txType));
    }

    @PostMapping("/insights/{dateStr}")
    public ResponseEntity<InsightResponseDTO> getInsight(@PathVariable("dateStr") String dateStr,
                                                         @RequestBody InsightPreferenceDTO dto) {
        return ResponseEntity.ok(transactionService.getInsightForAccount(dto.getAccountId(), LocalDate.parse(dateStr), dto));
    }

}
