package io.github.chait.transaction.service;

import io.github.chait.transaction.dto.InsightPreferenceDTO;

import java.time.LocalDate;

public interface AccountPreferenceService {

    void saveAccountPreference(InsightPreferenceDTO dto, LocalDate date);

    InsightPreferenceDTO getPreference(String accountId);

}
