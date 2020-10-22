package io.baselogic.springsecurity.web.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * A form object that is used for Sending and receiving Command Messages.
 *
 * @author mickknutson
 *
 * @since chapter14.01 Created.
 *
 */
@Data
@Builder
public class CommandDto {

    @NotEmpty(message = "Message is required")
    private String message;
    @NotEmpty(message = "Summary is required")
    private String summary;
    @NotEmpty(message = "Description is required")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull(message = "Event Date/Time is required")
    private LocalDateTime when;

} // The End...
