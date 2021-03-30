package io.github.chait.transaction.repo;

import io.github.chait.transaction.docs.AccountMonthlyPreferenceDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AccountPreferenceEsRepo extends ElasticsearchRepository<AccountMonthlyPreferenceDoc, String> {
}
