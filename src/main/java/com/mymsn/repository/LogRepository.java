package com.mymsn.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mymsn.entities.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID> {

}
