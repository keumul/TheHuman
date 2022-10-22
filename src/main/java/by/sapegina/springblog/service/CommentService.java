package by.sapegina.springblog.service;

import by.sapegina.springblog.dto.CommentsDto;
import by.sapegina.springblog.entity.Comment;
import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.exceptions.PostNotFoundException;
import by.sapegina.springblog.mapper.CommentsMapper;
import by.sapegina.springblog.repository.CommentRepository;
import by.sapegina.springblog.repository.PostRepository;
import by.sapegina.springblog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentsMapper commentsMapper;
    private final CommentRepository commentRepository;
    public void save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId()).
                orElseThrow(()->new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentsMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentsMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()->new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentsMapper::mapToDto)
                .collect(toList());

    }
}
