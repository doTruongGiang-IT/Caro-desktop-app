package com.sgu.caro.repository;

import com.sgu.caro.entity.User;
import java.util.List;

public interface UserRepositoryCustom {
    List<User> getById(long id);
}