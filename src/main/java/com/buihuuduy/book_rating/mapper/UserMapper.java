package com.buihuuduy.book_rating.mapper;

import com.buihuuduy.book_rating.DTO.request.UserEntityRequest;
import com.buihuuduy.book_rating.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserEntity toUser(UserEntityRequest userEntityRequest);
    //UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget UserEntity user, UserEntityRequest userUpdateRequest);
}
