package com.chandan.inventorymanagement.service;

import com.chandan.inventorymanagement.entity.User;

public interface UserService {

    User createUser(User user);

    String deleteUser(Long userId);
}
