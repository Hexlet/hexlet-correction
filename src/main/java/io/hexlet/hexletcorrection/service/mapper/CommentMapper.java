package io.hexlet.hexletcorrection.service.mapper;

import io.hexlet.hexletcorrection.domain.*;
import io.hexlet.hexletcorrection.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {CorrectionMapper.class, CorrecterMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "correction.id", target = "correctionId")
    @Mapping(source = "correcter.id", target = "correcterId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "correctionId", target = "correction")
    @Mapping(source = "correcterId", target = "correcter")
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
