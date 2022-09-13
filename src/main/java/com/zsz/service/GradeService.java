package com.zsz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsz.pojo.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {
    Page<Grade> getGradesData(Page<Grade> gradePage, String gradeName);

    List<Grade> getGrades();

}

