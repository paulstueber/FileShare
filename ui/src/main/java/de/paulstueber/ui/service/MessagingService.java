package de.paulstueber.ui.service;

import de.paulstueber.ui.model.FSEntity;
import de.paulstueber.ui.model.message.Event;
import de.paulstueber.ui.routes.Routes;
import de.paulstueber.ui.utils.CamelUtils;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service to handle any update/create/delete messaging to publish details to a notification topic
 */
@Service
public class MessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingService.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private CamelUtils camelUtils;

    /**
     * send an Event with UPDATE action
     * @param fsEntity entity to send as content
     */
    public void update(@NonNull final FSEntity fsEntity) {
        this.send(createEvent(fsEntity, Event.Action.UPDATE));
    }

    /**
     * send an Event with CREATE action
     * @param fsEntity entity to send as content
     */
    public void create(@NonNull final FSEntity fsEntity) {
        this.send(createEvent(fsEntity, Event.Action.CREATE));
    }

    /**
     * send an Event with RECREATE action
     * @param fsEntity entity to send as content
     */
    public void recreate(@NonNull final FSEntity fsEntity) {
        this.send(createEvent(fsEntity, Event.Action.RECREATE));
    }

    /**
     * send an Event with DELETE action
     * @param fsEntity entity to send as content
     */
    public void delete(@NonNull final FSEntity fsEntity) {
        this.send(createEvent(fsEntity, Event.Action.DELETE));
    }

    /**
     * send an Event with SAVE action
     * @param fsEntity entity to send as content
     */
    public void save(@NonNull final FSEntity fsEntity) {
        this.send(createEvent(fsEntity, Event.Action.SAVE));
    }

    private void send(@NonNull final Event dto) {
        LOGGER.debug("Sending {} message for {} with ID: {}",
                dto.getAction(), dto.getContent().getClass(), dto.getContent().getId());
        this.camelUtils.forward(Routes.ROUTE_DIRECT_SEND_DTO, dto);
    }

    private Event createEvent(@NonNull final FSEntity fsEntity, @NonNull final Event.Action action) {
        return new Event(serviceName, action, fsEntity);
    }

}
