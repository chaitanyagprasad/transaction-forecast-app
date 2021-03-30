package io.github.chait.transaction.mapper;

import io.github.chait.transaction.docs.TagsDoc;
import io.github.chait.transaction.dto.TagsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagsMapper {

    TagsDto mapToDto(TagsDoc doc);

}
