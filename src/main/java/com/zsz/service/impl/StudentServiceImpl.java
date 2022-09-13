package com.zsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsz.mapper.StudentMapper;
import com.zsz.pojo.Admin;
import com.zsz.pojo.LoginForm;
import com.zsz.pojo.Student;
import com.zsz.service.StudentService;
import com.zsz.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student login(LoginForm loginForm) {
        //创建QueryWapper对象
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        //拼接查询条件
        studentQueryWrapper.eq("name",loginForm.getUsername());
        studentQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(studentQueryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("id",userId);
        return baseMapper.selectOne(studentQueryWrapper);
    }

    @Override
    public Page<Student> getStudentData(Page<Student> studentPage, Student student) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getClazzName())){
            studentQueryWrapper.like("clazz_name",student.getClazzName());
        }
        if (!StringUtils.isEmpty(student.getName())){
            studentQueryWrapper.like("name",student.getName());
        }
        Page<Student> studentPage1 = baseMapper.selectPage(studentPage, studentQueryWrapper);
        return studentPage1;
    }
}
