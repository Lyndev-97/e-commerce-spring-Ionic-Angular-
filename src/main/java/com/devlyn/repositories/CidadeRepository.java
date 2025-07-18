package com.devlyn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlyn.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository <Cidade, Integer> {

}
