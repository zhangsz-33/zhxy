package com.zsz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsz.pojo.LoginForm;
import com.zsz.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Student getStudentById(Long userId);

    Page<Student> getStudentData(Page<Student> studentPage, Student student);
}

