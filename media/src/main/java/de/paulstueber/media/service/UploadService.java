package de.paulstueber.media.service;

import de.paulstueber.media.model.Media;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * upload service
 */
@Service
public class UploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    @Value("${upload.folder.files}")
    private String uploadFolder;

    @Autowired
    private MediaService mediaService;


    @PostConstruct
    private void init() {
        // create upload dir if it does not exist
        File uploadDir = new File(uploadFolder);
        LOGGER.debug("Upload dir {} exists: {}", uploadFolder, uploadDir.exists());
        LOGGER.info("Upload dir {} created: {}", uploadFolder, uploadDir.mkdir());
    }

    /**
     *
     * @param multipartFile
     * @return
     * @throws IOException thrown if the file could not be stored or processed
     * @throws URISyntaxException invalid uri was used/created
     */
    public Media uploadFile(final MultipartFile multipartFile)
            throws IOException, URISyntaxException {
        String originalFilename = multipartFile.getOriginalFilename();

        LOGGER.debug("Processing upload: {}", originalFilename);

        String newFilename = this.generateNewFilename(originalFilename);
        String resourceLocation = uploadFolder + newFilename;
        String contentType = multipartFile.getContentType();

        File newFile = new File(resourceLocation);
        multipartFile.transferTo(newFile);

        Media media = Media.builder()
                .filename(newFilename)
                .originalFilename(originalFilename)
                .resourceLocation(resourceLocation)
                .contentType(contentType)
                .size(multipartFile.getSize()).build();
        return this.mediaService.save(media);
    }

    private String generateNewFilename(@NonNull final String originalFilename) {
        String newFilenameBase = UUID.randomUUID().toString();
        String originalFileExtension = FilenameUtils.getExtension(originalFilename);
        return newFilenameBase + "." + originalFileExtension;
    }
}
