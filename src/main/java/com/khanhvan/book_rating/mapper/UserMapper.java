package com.khanhvan.book_rating.mapper;

import com.khanhvan.book_rating.DTO.request.UserInfoRequest;
import com.khanhvan.book_rating.DTO.response.UserInfoResponse;
import com.khanhvan.book_rating.DTO.request.UserEntityRequest;
import com.khanhvan.book_rating.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserEntity toUser(UserEntityRequest userEntityRequest);
    void updateUser(@MappingTarget UserEntity user, UserInfoRequest userUpdateRequest);
    UserInfoResponse toUserInfo(UserEntity userEntity);
}
