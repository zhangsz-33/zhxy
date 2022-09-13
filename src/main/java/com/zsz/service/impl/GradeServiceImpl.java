package com.zsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsz.mapper.GradeMapper;
import com.zsz.pojo.Grade;
import com.zsz.service.GradeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public Page<Grade> getGradesData(Page<Grade> gradePage, String gradeName) {
        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)){
            gradeQueryWrapper.like("name",gradeName);
        }
        gradeQueryWrapper.orderByDesc("id");
        Page<Grade> page = baseMapper.selectPage(gradePage, gradeQueryWrapper);
        return page;
    }

    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);
    }
}
