package com.zsz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsz.pojo.Student;
import com.zsz.service.StudentService;
import com.zsz.util.MD5;
import com.zsz.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    StudentService studentService;

    //   http://localhost:8080/sms/studentController/getStudentByOpr/1/3?clazzName=&name=
    @ApiOperation("根据分页条件获取学生分页数据")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("学生信息") Student student
    ){
        Page<Student> studentPage = new Page<>(pageNo,pageSize);
        Page<Student> studentPage1 = studentService.getStudentData(studentPage,student);
        return Result.ok(studentPage1);
    }

    //  http://localhost:8080/sms/studentController/addOrUpdateStudent
    @ApiOperation("新增或更改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @RequestBody Student student
    ){
        Integer id = student.getId();
        if (null == id || 0 == id){
            String password = student.getPassword();
            String encrypt = MD5.encrypt(password);
            student.setPassword(encrypt);
        }

        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    //  http://localhost:8080/sms/studentController/delStudentById
    @ApiOperation("删除单个或多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }
}
