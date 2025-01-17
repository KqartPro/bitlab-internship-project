package kz.pryahin.bitlabInternship.minio.mappers;

import kz.pryahin.bitlabInternship.minio.dtos.AttachmentDto;
import kz.pryahin.bitlabInternship.minio.entities.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {


    @Mapping(target = "lessonId", ignore = true)
    AttachmentDto mapToDto(Attachment attachment);
}
