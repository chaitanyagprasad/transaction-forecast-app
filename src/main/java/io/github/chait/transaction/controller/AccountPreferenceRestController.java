package io.github.chait.transaction.controller;

import io.github.chait.transaction.dto.InsightPreferenceDTO;
import io.github.chait.transaction.service.AccountPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/preference")
public class AccountPreferenceRestController {

    @Autowired
    AccountPreferenceService accountPreferenceService;

    @PostMapping("/{dateStr}")
    public void setPreference(@RequestBody InsightPreferenceDTO dto,@PathVariable("dateStr") String dateStr) {
        this.accountPreferenceService.saveAccountPreference(dto, LocalDate.parse(dateStr));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<InsightPreferenceDTO> getPreference(@PathVariable("accountId") String accountId) {
        return ResponseEntity.ok(accountPreferenceService.getPreference(accountId));
    }

}
