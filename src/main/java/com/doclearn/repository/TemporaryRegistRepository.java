package com.doclearn.repository;


import com.doclearn.model.TemporaryRegToDel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryRegistRepository extends JpaRepository<TemporaryRegToDel,Long> {


    Optional<TemporaryRegToDel> findByEmail(String email);
}
