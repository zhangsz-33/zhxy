package com.zsz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsz.pojo.Admin;
import com.zsz.service.AdminService;
import com.zsz.util.MD5;
import com.zsz.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("管理员管理器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    AdminService adminService;

    //   http://localhost:8080/sms/adminController/getAllAdmin/1/3?adminName=
    @ApiOperation("根据分页条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("要查询的管理员姓名") String adminName
    ){
        Page<Admin> adminPage = new Page<>(pageNo, pageSize);
        Page<Admin> adminPage1 = adminService.getAllAdminData(adminPage,adminName);
        return Result.ok(adminPage1);
    }

    //   http://localhost:8080/sms/adminController/saveOrUpdateAdmin
    @ApiOperation("新增或者修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("提交的管理员信息") @RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if (null == id || 0 == id){
            String password = admin.getPassword();
            String encrypt = MD5.encrypt(password);
            admin.setPassword(encrypt);
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    //   http://localhost:8080/sms/adminController/deleteAdmin
    @ApiOperation("删除单个或多个管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("要删除的管理员的id列表") @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
