package com.yusufaydin.FileStorage.mapper;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toDocument(DocumentDto documentDto);

    @Mapping(target = "fileContent", ignore = true)
    DocumentDto toDocumentDto(Document document);
}
