package by.sapegina.springblog.service;

import by.sapegina.springblog.dto.CommentsDto;
import by.sapegina.springblog.entity.Comment;
import by.sapegina.springblog.entity.Email;
import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.exceptions.PostNotFoundException;
import by.sapegina.springblog.exceptions.TheHumanException;
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
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentsMapper commentsMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    public void save(CommentsDto commentsDto){
        Post post = postRepository.findById(commentsDto.getPostId()).
                orElseThrow(()->new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentsMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentsMapper::mapToDto).collect(toList());
    }
    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new Email(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }
    public List<CommentsDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()->new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentsMapper::mapToDto)
                .collect(toList());

    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("fucking shit your mother is dog shit fucking shitty shit")) {
            throw new TheHumanException("Comments contains unacceptable language");
        }
        return false;
    }
}
