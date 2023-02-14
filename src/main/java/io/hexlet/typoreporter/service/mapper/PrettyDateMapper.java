package io.hexlet.typoreporter.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;


@Mapper
public interface PrettyDateMapper {

    PrettyTime prettyTime = new PrettyTime();

    @Named(value = "mapToPrettyDateAgo")
    default String getDateAgoAsString(LocalDateTime date) {
        return prettyTime.format(date);
    }
}
