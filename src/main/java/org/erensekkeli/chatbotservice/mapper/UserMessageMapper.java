package org.erensekkeli.chatbotservice.mapper;

import org.erensekkeli.chatbotservice.dto.UserMessageDTO;
import org.erensekkeli.chatbotservice.entity.UserMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring")
public interface UserMessageMapper {

    UserMessageMapper INSTANCE = Mappers.getMapper(UserMessageMapper.class);

    @Mapping(source = "session.id", target = "sessionId")
    UserMessageDTO convertToUserMessageDTO(UserMessage userMessage);

    List<UserMessageDTO> convertToUserMessageDTOs(List<UserMessage> userMessages);
}
