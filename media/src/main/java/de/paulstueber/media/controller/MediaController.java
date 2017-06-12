package de.paulstueber.media.controller;

import de.paulstueber.media.exception.EntityNotFoundException;
import de.paulstueber.media.model.Media;
import de.paulstueber.media.service.MediaService;
import de.paulstueber.media.service.UploadService;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * asset REST controller
 */
@Api(value = "/files")
@RestController
@RequestMapping("/files")
public class MediaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private MediaService mediaService;

    @ResponseBody
    @RequestMapping(value = "/upload/multipart",
            method = RequestMethod.POST,
            produces = "application/json")
    private ResponseEntity<Media> uploadFile(@RequestParam final MultipartFile file)
            throws IOException, URISyntaxException {

        Media media = this.uploadService.uploadFile(file);
        return new ResponseEntity<>(media, HttpStatus.OK);
    }


    /**
     * Upload endpoint collection multipart http requests and processing uploads
     *
     * @param request multipart http servlet request
     * @return Map containing all uploaded files
     * @throws IOException thrown if the file could not be stored or processed
     * @throws URISyntaxException invalid uri was used/created
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<Map> upload(final MultipartHttpServletRequest request)
            throws IOException, URISyntaxException {
        LOGGER.debug("uploadPost called");
        Iterator<String> itr = request.getFileNames();
        List<Media> list = new LinkedList<>();

        while (itr.hasNext()) {
            Media media = this.uploadService.uploadFile(request.getFile(itr.next()));
            list.add(media);
        }

        Map<String, Object> files = new HashMap<>();
        files.put("files", list);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    /**
     *get a file (binary) by its upload id
     * @param mediaId media id aka. upload id
     * @return InputStreamResource for files content
     * @throws IOException thrown if the file could not be stored or processed
     * @throws EntityNotFoundException thrown if entity was not found
     */
    @RequestMapping(value = "/{mediaId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFile(@PathVariable final String mediaId)
            throws IOException, EntityNotFoundException {
        LOGGER.debug("Get media file by ID {}", mediaId);
        Media media = this.mediaService.findOne(mediaId);
        return this.mapToInputStreamResource(media);
    }

    /**
     * Get all files from DB b< offset and limit
     * @param offset offset (starts at 0)
     * @param size page-size (default 10)
     * @return page object with list of files
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Media> getAllByOffset(@RequestParam(defaultValue = "0") final int offset,
                                      @RequestParam(defaultValue = "10") final int size) {
        return mediaService.findAll(offset / size, size);
    }

    private ResponseEntity<InputStreamResource> mapToInputStreamResource(@NonNull final Media media)
            throws FileNotFoundException {
        InputStream fileInputStream = new FileInputStream(media.getResourceLocation());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(media.getSize())
                .contentType(MediaType.parseMediaType(media.getContentType()))
                .body(new InputStreamResource(fileInputStream));
    }
}
