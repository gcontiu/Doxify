package com.worldpay.hackathon.repository;

import org.springframework.data.repository.CrudRepository;

import com.worldpay.hackathon.data.Author;

public interface AuthorRepository extends CrudRepository<Author, String> {

    Author findByUserName(String userName);

    long count();

}
