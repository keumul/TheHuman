package by.sapegina.springblog.service;

import by.sapegina.springblog.dto.PostRequest;
import by.sapegina.springblog.dto.PostResponse;
import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.exceptions.PostNotFoundException;
import by.sapegina.springblog.mapper.PostMapper;
import by.sapegina.springblog.repository.PostRepository;
import by.sapegina.springblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public Post save(PostRequest postRequest) {
        User currentUser = authService.getCurrentUser();
        return postMapper.map(postRequest, currentUser);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
