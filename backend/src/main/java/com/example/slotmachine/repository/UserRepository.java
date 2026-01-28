package com.example.slotmachine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.slotmachine.model.User;

// JpaRepository<User, Long> 的意思是：
// 这是一个专门管理 User 这种泥人的管理员，泥人的身份证号是 Long 类型的数字。
// 只要继承了这个接口，Spring Boot 就会自动送给我们很多方法，比如 save(保存), findById(找人), delete(删除)
public interface UserRepository extends JpaRepository<User, Long> {

    // 我们自己加一个功能：根据名字找人
    // Optional 的意思是：可能找得到，也可能找不到（比如查无此人）
    Optional<User> findByUsername(String username);
}
