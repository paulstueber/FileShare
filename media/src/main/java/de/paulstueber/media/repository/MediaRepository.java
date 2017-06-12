package de.paulstueber.media.repository;

import de.paulstueber.media.model.Media;
import de.paulstueber.media.model.Status;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Media repository to perform DB actions on the media collection
 */
public interface MediaRepository extends MongoRepository<Media, String> {

    /**
     * Find all media entities by status
     *
     * @param status
     * @return list of media entities
     */
    List<Media> findByStatus(@NonNull final Status status);

    /**
     * Find all media entities by status
     *
     * @param filename file name
     * @return list of media entities
     */
    Optional<Media> findByFilename(@NonNull final String filename);
}
