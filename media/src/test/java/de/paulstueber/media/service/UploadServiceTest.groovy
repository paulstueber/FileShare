package de.paulstueber.media.service

import de.paulstueber.media.model.Media
import de.paulstueber.media.repository.MediaRepository
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

/**
 * Created by paul on 12.06.17.
 */
class UploadServiceTest extends Specification {

    def mediaService = Mock(MediaService){};
    def underTest = new UploadService(
            uploadFolder: "uploads/",
            mediaService: mediaService
    )

    def "UploadFile: media object should be created for uploaded file"() {
        setup:
        def filename = "filename.png"
        def size = 123456
        def multipartFile = Mock(MultipartFile) {
            getOriginalFilename() >> filename
            getContentType() >> "image/png"
            getSize() >> size
        }

        when:
        def result = underTest.uploadFile(multipartFile)

        then:
        1 * mediaService.save(_ as Media) >> {args -> args[0]}
        result.getSize() == size
    }

    def "UploadFile: NullPointerException should be thrown if ORIGINALFILENAME was not set/is null"() {
        setup:
        def size = 123456
        def multipartFile = Mock(MultipartFile) {
            getOriginalFilename() >> null
            getContentType() >> "image/png"
            getSize() >> size
        }

        when:
        def result = underTest.uploadFile(multipartFile)

        then:
        thrown(NullPointerException)
    }
}
