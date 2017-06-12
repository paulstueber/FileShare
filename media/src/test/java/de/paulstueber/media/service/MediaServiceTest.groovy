package de.paulstueber.media.service

import de.paulstueber.media.exception.EntityNotFoundException
import de.paulstueber.media.model.Media
import de.paulstueber.media.model.Status
import de.paulstueber.media.repository.MediaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class MediaServiceTest extends Specification {

    def mediaRepository = Mock(MediaRepository){};
    def underTest = new MediaService(
            mediaRepository: mediaRepository
    )

    def "findOne: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.findOne(null)

        then:
        thrown(NullPointerException)
    }

    def "FindOne: should return a media entity with given id"() {
        setup:
        def id = "123456"
        def media = Media.builder().id(id).build()

        when:
        def result = underTest.findOne(id)

        then:
        1 * mediaRepository.findOne(_ as String) >> media
        result == media
    }

    def "FindOne: EntityNotFoundException should be thrown if not entity with given ID was found"() {
        setup:
        def id = "123456"
        def media = Media.builder().id(id).build()

        when:
        underTest.findOne(id)

        then:
        1 * mediaRepository.findOne(_ as String) >> null
        thrown(EntityNotFoundException)
    }

    def "FindByStatus: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.findByStatus(null)

        then:
        thrown(NullPointerException)
    }

    def "FindByStatus: all entities with given status should be returned"() {
        when:
        def media = underTest.findByStatus(Status.NEW)

        then:
        1 * mediaRepository.findByStatus(_ as Status) >> [Mock(Media), Mock(Media)]
        media.size() == 2
    }

    def "FindByFilename: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.findByFilename(null)

        then:
        thrown(NullPointerException)
    }

    def "FindByFilename: EntityNotFoundException should be thrown if not entity with given filename was found"() {
        setup:
        def filename = "filename.png"
        def media = Media.builder().filename(filename).build()

        when:
        underTest.findByFilename(filename)

        then:
        1 * mediaRepository.findByFilename(filename) >> Optional.empty()
        thrown(EntityNotFoundException)
    }

    def "FindByFilename: should return a media entity with given file name"() {
        setup:
        def filename = "filename.png"
        def media = Media.builder().filename(filename).build()

        when:
        def result = underTest.findByFilename(filename)

        then:
        1 * mediaRepository.findByFilename(filename) >> Optional.of(media)
        result.getFilename().equals(media.getFilename())
    }

    def "Delete: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.delete(null)

        then:
        thrown(NullPointerException)
    }
    def "Delete: when delete is called, the repository.delete method should be called once with the given id"() {
        setup:
        def id = "123456"

        when:
        underTest.delete(id)

        then:
        1 * mediaRepository.delete(id)
    }

    def "FindAll: IllegalArgumentException should be thrown if PAGE parameter is invalid"() {
        when:
        underTest.findAll(0, 0)

        then:
        thrown(IllegalArgumentException)
    }

    def "FindAll: IllegalArgumentException should be thrown if SIZE parameter is invalid"() {
        when:
        underTest.findAll(1, 0)

        then:
        thrown(IllegalArgumentException)
    }

    def "FindAll: page object with correct number of elements should be returned"() {
        setup:
        def page = new PageImpl<Media>(
                [Mock(Media), Mock(Media)],
                new PageRequest(1, 2),
                2
        )

        when:
        def result = underTest.findAll(1, 2)

        then:
        1 * mediaRepository.findAll(_ as Pageable) >> page
        result.totalElements == page.totalElements
    }

    def "Save: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.save(null)

        then:
        thrown(NullPointerException)
    }

    def "Save: repository SAVE should be called once"() {
        when:
        def result = underTest.save(Mock(Media))

        then:
        1 * mediaRepository.save(_ as Media) >> Mock(Media)
    }

    def "Update: NullPointerException should be thrown when called with NULL parameter"() {
        when:
        underTest.update(id, media)

        then:
        thrown(NullPointerException)

        where:
        id                 |   media
        null               |   null
        "123456"           |   null
    }

    def "Update: EntityNotFoundException should be thrown if no media with given ID was found"() {
        setup:
        def id = "123456"
        def media = Media.builder().id(id).build()

        when:
        underTest.update(id, media)

        then:
        1 * mediaRepository.findOne(id) >> null
        thrown(EntityNotFoundException)
    }

    def "Update: Status field should be updated on valid, incoming update request"() {
        setup:
        def id = "123456"
        def media = Media.builder().id(id).status(Status.NEW).build()

        def mediaUpdate = Media.builder().id(id).status(Status.COMPLETE).build()

        when:
        def result = underTest.update(id, mediaUpdate)

        then:
        1 * mediaRepository.findOne(id) >> media
        1 * mediaRepository.save(media) >> media
        media.getStatus() == mediaUpdate.getStatus();
    }
}
