package by.sapegina.springblog.service;

import by.sapegina.springblog.dto.PostRequest;
import by.sapegina.springblog.dto.PostResponse;
import by.sapegina.springblog.entity.Comment;
import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.entity.Vote;
import by.sapegina.springblog.exceptions.PostNotFoundException;
import by.sapegina.springblog.mapper.CommentsMapper;
import by.sapegina.springblog.mapper.PostMapper;
import by.sapegina.springblog.repository.CommentRepository;
import by.sapegina.springblog.repository.PostRepository;
import by.sapegina.springblog.repository.UserRepository;
import by.sapegina.springblog.repository.VoteRepository;
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
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    public void save(PostRequest postRequest) {
        postRepository.save(postMapper.map(postRequest, authService.getCurrentUser()));
    }
    @Transactional
    public void delete(Long id) {
        for (Comment comment : commentRepository.findByPost(postRepository.getById(id))) {
            commentRepository.delete(comment);
        }
        for (Vote vote : voteRepository.findByPost(postRepository.getById(id))){
            voteRepository.delete(vote);
        }
        postRepository.delete(postRepository.getById(id));

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
