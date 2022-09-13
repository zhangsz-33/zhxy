package com.zsz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsz.pojo.Grade;
import com.zsz.service.GradeService;
import com.zsz.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    GradeService gradeService;

    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }


    //请求路径 /sms/gradeController/getGrades/1/3?gradeName=

    /**
     * 年级管理页面分页展示数据，查询以及重置方法
     * @param pageNo
     * @param pageSize
     * @param gradeName
     * @return
     */
    @ApiOperation("根据年级名称模糊查询，带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(@ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
                            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
                            @ApiParam("分页查询模糊匹配的名称") String gradeName){

        //设置分页信息
        Page<Grade> gradePage = new Page<>(pageNo, pageSize);
        //执行服务层方法，查询出数据
        Page<Grade> page = gradeService.getGradesData(gradePage,gradeName);

        return Result.ok(page);
    }


    /**
     * 年级修改和添加方法
     * @param grade
     * @return
     */
    @ApiOperation("新增或修改grade,有id属性是修改，没有则是增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("Json格式的Grade对象") @RequestBody Grade grade){
        gradeService.saveOrUpdate(grade);

        return Result.ok();
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的所有的grade的id的Json集合") @RequestBody List<Integer> id){

        gradeService.removeByIds(id);
        return Result.ok();
    }
}
