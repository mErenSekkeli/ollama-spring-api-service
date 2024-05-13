package org.erensekkeli.chatbotservice.mapper;

import org.erensekkeli.chatbotservice.dto.SessionDTO;
import org.erensekkeli.chatbotservice.dto.SessionUpdateRequest;
import org.erensekkeli.chatbotservice.entity.Session;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "baseAdditionalFields.createDate", target = "createDate")
    SessionDTO convertToSessionDTO(Session session);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "baseAdditionalFields.createDate", target = "createDate")
    List<SessionDTO> convertToSessionDTOs(List<Session> sessions);

    @Mapping(target = "id", ignore = true)
    void updateSessionFields(@MappingTarget Session session, SessionUpdateRequest request);
}
