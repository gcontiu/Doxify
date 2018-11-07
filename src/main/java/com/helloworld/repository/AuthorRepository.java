package com.helloworld.repository;

import org.springframework.data.repository.CrudRepository;

import com.helloworld.data.Author;

public interface AuthorRepository extends CrudRepository<Author, String> {

    Author findByUserName(String userName);
}
