package by.sapegina.springblog.mapper;

import by.sapegina.springblog.dto.PostRequest;
import by.sapegina.springblog.dto.PostResponse;
import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}
