package com.zsz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsz.pojo.Clazz;

import java.util.List;

public interface ClazzService extends IService<Clazz> {
    Page<Clazz> getClazzData(Page<Clazz> clazzPage, Clazz clazz);

    List<Clazz> getClazzs();
}
