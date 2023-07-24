package com.yusufaydin.FileStorage.mapper;

import com.yusufaydin.FileStorage.dto.DocumentDto;
import com.yusufaydin.FileStorage.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Document toDocument(DocumentDto documentDto);

    @Mapping(target = "fileContent", ignore = true)
    DocumentDto toDocumentDto(Document document);
}
