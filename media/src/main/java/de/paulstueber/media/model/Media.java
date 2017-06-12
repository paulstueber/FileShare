package de.paulstueber.media.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Media entity
 */
@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {

    @Id
    private String id;
    private String filename;
    private String originalFilename;
    private String resourceLocation;
    private String contentType;
    private long size;
    private Status status = Status.NEW;
}
