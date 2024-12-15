package com.example.skillConnectBackend.repository;

import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.responses.PostResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
   
    List<Post> findAllBySkillsId(Long skillId);
    @Query("SELECT p FROM Post p JOIN p.skills s WHERE s.id IN :skillIds")
    List<Post> findBySkillIds(@Param("skillIds") List<Long> skillIds);
    @Query("SELECT p FROM Post p JOIN FETCH p.user u WHERE u.id = :userId")
    List<Post> findPostsByUserId(@Param("userId") Long userId);
    @Query("SELECT PostResponse(p.id, p.content, p.timestamp, u.username, p.skills) " +
    	       "FROM Post p JOIN p.user u")
    	List<PostResponse> findAllWithUsernames();
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.taggedUsers WHERE p.id = :postId")
    Optional<Post> findPostWithTaggedUsers(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.taggedUsers")
    List<Post> findAllPostsWithTaggedUsers();

}
