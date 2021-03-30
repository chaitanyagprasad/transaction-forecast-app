package io.github.chait.transaction.mapper;

import io.github.chait.transaction.docs.AccountMonthlyPreferenceDoc;
import io.github.chait.transaction.dto.InsightPreferenceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountPreferenceMapper {

    AccountMonthlyPreferenceDoc mapDtoToDoc(InsightPreferenceDTO dto);

    InsightPreferenceDTO mapDocToDto(AccountMonthlyPreferenceDoc doc);

}
