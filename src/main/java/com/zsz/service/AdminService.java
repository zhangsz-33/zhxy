package com.zsz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsz.pojo.Admin;
import com.zsz.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    Page<Admin> getAllAdminData(Page<Admin> adminPage, String adminName);
}
