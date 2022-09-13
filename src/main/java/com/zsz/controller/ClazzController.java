package com.zsz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsz.pojo.Clazz;
import com.zsz.service.ClazzService;
import com.zsz.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    @ApiOperation("获取所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzs = clazzService.getClazzs();
        return Result.ok(clazzs);
    }


    //     http://localhost:8080/sms/clazzController/getClazzsByOpr/1/3?gradeName=&name=
    @ApiOperation("根据年级名称和班级名称获取班级信息，带分页")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("班级信息") Clazz clazz
    ){
        Page<Clazz> clazzPage = new Page<>(pageNo, pageSize);
        Page<Clazz> page = clazzService.getClazzData(clazzPage,clazz);
        return Result.ok(page);
    }

    @ApiOperation("增加或修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("Json格式的班级信息") @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("数组格式的班级ID") @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }
}
