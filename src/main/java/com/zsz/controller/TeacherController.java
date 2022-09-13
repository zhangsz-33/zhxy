package com.zsz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsz.pojo.Teacher;
import com.zsz.service.TeacherService;
import com.zsz.util.MD5;
import com.zsz.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("教师管理器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    //   http://localhost:8080/sms/teacherController/getTeachers/1/3?clazzName=&name=
    @ApiOperation("根据分页条件获取教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的班级名称和教师名称") Teacher teacher
    ){
        Page<Teacher> teacherPage = new Page<>(pageNo, pageSize);
        Page<Teacher> teacherPage1 = teacherService.getTeacherData(teacherPage,teacher);
        return Result.ok(teacherPage1);
    }

    //  http://localhost:8080/sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("新增或者更改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("要提交的教师信息") @RequestBody Teacher teacher
    ){
        Integer id = teacher.getId();
        if (null == id || 0 == id){
            String password = teacher.getPassword();
            String encrypt = MD5.encrypt(password);
            teacher.setPassword(encrypt);
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    //   http://localhost:8080/sms/teacherController/deleteTeacher
    @ApiOperation("删除单个或多个老师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("要删除的教师id列表") @RequestBody List<Integer> ids
    ){

        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
