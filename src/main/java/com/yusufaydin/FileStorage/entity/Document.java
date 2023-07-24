package com.yusufaydin.FileStorage.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "document")
public class Document extends BaseEntity {
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_content", columnDefinition = "TEXT")
    private String fileContent;

    @Column(name = "reference_source")
    private String referenceSource;

    @Column(name = "reference_key")
    private String referenceKey;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_key")
    private String documentKey;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "document_tags", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "tag")
    private List<String> tags;

}
