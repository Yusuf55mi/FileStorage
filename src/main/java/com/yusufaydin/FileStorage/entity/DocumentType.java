package com.yusufaydin.FileStorage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "document_type")
public class DocumentType extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "maxFileSize")
    private int maxFileSize;
    @Column(name = "maxFileCount")
    private Integer maxFileCount;
    @Column(name = "allowedMimeTypes")
    private String allowedMimeTypes;
}
