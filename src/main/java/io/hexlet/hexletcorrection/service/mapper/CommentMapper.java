package io.hexlet.hexletcorrection.service.mapper;


import io.hexlet.hexletcorrection.domain.Comment;
import io.hexlet.hexletcorrection.service.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PreferenceMapper.class, CorrectionMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "correction.id", target = "correctionId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "correctionId", target = "correction")
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
