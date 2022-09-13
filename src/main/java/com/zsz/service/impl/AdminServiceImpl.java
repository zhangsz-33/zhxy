package com.zsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsz.mapper.AdminMapper;
import com.zsz.pojo.Admin;
import com.zsz.pojo.LoginForm;
import com.zsz.service.AdminService;
import com.zsz.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        //创建QueryWrapper对象
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        //拼接查询条件
        adminQueryWrapper.eq("name",loginForm.getUsername());
        //将密码转换成密文进行查询
        adminQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(adminQueryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("id",userId);
        return baseMapper.selectOne(adminQueryWrapper);
    }

    @Override
    public Page<Admin> getAllAdminData(Page<Admin> adminPage, String adminName) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)){
            adminQueryWrapper.like("name", adminName);
        }
        Page<Admin> page = baseMapper.selectPage(adminPage, adminQueryWrapper);
        return page;
    }
}
