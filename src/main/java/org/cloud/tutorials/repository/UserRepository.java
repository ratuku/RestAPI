package org.cloud.tutorials.repository;

import org.cloud.tutorials.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
