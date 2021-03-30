package io.github.chait.transaction.mapper;

import io.github.chait.transaction.docs.TransactionDoc;
import io.github.chait.transaction.dto.TransactionDTO;
import io.github.chait.transaction.entity.TransactionEntity;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface TransactionMapper {


    /*TransactionDTO map(TransactionEntity input);

    TransactionEntity map(TransactionDTO input);*/

    TransactionDoc mapToDoc(TransactionDTO input);

    TransactionDTO mapDocToDto(TransactionDoc doc);


}
