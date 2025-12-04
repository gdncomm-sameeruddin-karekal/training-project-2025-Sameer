package com.blibli.member.repository;

import com.blibli.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    Optional<MemberEntity> findByUserName(String userName);
    boolean existsByEmail(String email);

}
