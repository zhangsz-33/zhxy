package com.zsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsz.mapper.TeacherMapper;
import com.zsz.pojo.LoginForm;
import com.zsz.pojo.Teacher;
import com.zsz.service.TeacherService;
import com.zsz.util.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name",loginForm.getUsername());
        teacherQueryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("id",userId);
        return baseMapper.selectOne(teacherQueryWrapper);
    }

    @Override
    public Page<Teacher> getTeacherData(Page<Teacher> teacherPage, Teacher teacher) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            teacherQueryWrapper.like("clazz_name",teacher.getClazzName());
        }
        if (!StringUtils.isEmpty(teacher.getName())){
            teacherQueryWrapper.like("name",teacher.getName());
        }
        Page<Teacher> Page = baseMapper.selectPage(teacherPage, teacherQueryWrapper);
        return Page;
    }
}
