package org.cloud.tutorials.repository;

import org.cloud.tutorials.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
