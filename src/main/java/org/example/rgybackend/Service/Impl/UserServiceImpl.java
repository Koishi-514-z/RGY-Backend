package org.example.rgybackend.Service.Impl;

import org.example.rgybackend.DAO.UserDAO;
import org.example.rgybackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;

    
}
