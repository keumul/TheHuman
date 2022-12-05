package by.sapegina.springblog.repository;

import by.sapegina.springblog.entity.Post;
import by.sapegina.springblog.entity.User;
import by.sapegina.springblog.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
    List<Vote> findByPost(Post post);
}
