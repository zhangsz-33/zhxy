package com.zsz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsz.pojo.LoginForm;
import com.zsz.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getTeacherById(Long userId);

    Page<Teacher> getTeacherData(Page<Teacher> teacherPage, Teacher teacher);
}

