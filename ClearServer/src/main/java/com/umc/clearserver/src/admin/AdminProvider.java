package com.umc.clearserver.src.admin;

import com.umc.clearserver.src.friend.FriendDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminProvider {
    private final AdminDao adminDao;

    public AdminProvider(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());
}
