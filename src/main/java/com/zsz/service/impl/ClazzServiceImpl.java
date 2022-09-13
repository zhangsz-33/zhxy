package com.zsz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsz.mapper.ClazzMapper;
import com.zsz.pojo.Clazz;
import com.zsz.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public Page<Clazz> getClazzData(Page<Clazz> clazzPage, Clazz clazz) {
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clazz.getGradeName())){
            clazzQueryWrapper.like("grade_name",clazz.getGradeName());
        }
        if (!StringUtils.isEmpty(clazz.getName())){
            clazzQueryWrapper.like("name",clazz.getName());
        }
        Page<Clazz> selectPage = baseMapper.selectPage(clazzPage, clazzQueryWrapper);
        return selectPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();

        return baseMapper.selectList(clazzQueryWrapper);
    }
}
