package com.hr.repository;

import com.hr.entity.CreatePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatePostRepo extends JpaRepository<CreatePost,Integer>{
}
