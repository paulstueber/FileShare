package de.paulstueber.media.service;

import de.paulstueber.media.model.Media;
import de.paulstueber.media.model.Status;
import de.paulstueber.media.repository.MediaRepository;
import de.paulstueber.media.exception.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Media service class
 */
@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;


    /**
     * find and return a single entity
     *
     * @param id Media id
     * @return Media entity
     * @throws EntityNotFoundException thrown if file does not exist
     */
    public Media findOne(@NonNull final String id) throws EntityNotFoundException {
        return Optional.ofNullable(this.mediaRepository.findOne(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("File with id {} not found", id)));
    }

    /**
     * Get a list of media entities by status
     *
     * @param status Status
     * @return list of media entities that currently have the given status
     */
    public List<Media> findByStatus(@NonNull final Status status) {
        return this.mediaRepository.findByStatus(status);
    }

    /**
     * Get a list of media entities by status
     *
     * @param filename file name
     * @return list of media entities that currently have the given status
     * @throws EntityNotFoundException thrown if file does not exist
     */
    public Media findByFilename(@NonNull final String filename) throws EntityNotFoundException {
        return this.mediaRepository.findByFilename(filename)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File {} not found", filename)));
    }

    /**
     * delete a single entity
     *
     * @param id Media id
     */
    public void delete(@NonNull final String id) {
        this.mediaRepository.delete(id);
    }

    /**
     * get all available Media entities
     *
     * @param page page defining the offset
     * @param size page size
     * @return page object containing all Medias within the page(size)
     */
    public Page<Media> findAll(final int page, final int size) {
        Pageable pageRequest = new PageRequest(page, size);
        return this.mediaRepository.findAll(pageRequest);
    }

    /**
     * save Media to the DB (id will be set while saving)
     *
     * @param media to be stored
     * @return Media entity with unique id
     */
    public Media save(@NonNull final Media media) {
        return this.mediaRepository.save(media);
    }

    /**
     * update a media entity
     *
     * @param id unique id of media used to retrieve the original document from DB. Necessary if media.id is
     *           not set. Otherwise use MediaService#save(Media media)
     * @param media to be upldated
     * @return Media entity
     * @throws EntityNotFoundException thrown if the requested entity was not found in DB
     */
    public Media update(@NonNull final String id, @NonNull final Media media)
            throws EntityNotFoundException {
        Media m = this.mediaRepository.findOne(id);
        if (m == null) {
            throw new EntityNotFoundException(String.format("Media entity with id %s not found in DB", id));
        }
        m.setContentType(media.getContentType());
        m.setOriginalFilename(media.getOriginalFilename());
        m.setResourceLocation(media.getResourceLocation());
        m.setSize(media.getSize());
        m.setStatus(media.getStatus());
        return this.mediaRepository.save(m);
    }
}
