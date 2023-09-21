package org.example.mappers;

import org.example.domain.dto.UserVDTO;
import org.example.domain.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserVDTO toVDTO(AppUser appUser);
}
