package com.zsz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsz.pojo.Admin;
import com.zsz.pojo.LoginForm;
import com.zsz.pojo.Student;
import com.zsz.pojo.Teacher;
import com.zsz.service.AdminService;
import com.zsz.service.StudentService;
import com.zsz.service.TeacherService;
import com.zsz.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api("系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    //  http://localhost:8080/sms/system/headerImgUpload
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @RequestPart("multipartFile") MultipartFile multipartFile
    ){
        /*
         * 处理上传的文件名,这里写固定的路径
         * 真实开发环境中，将图片上传至图片服务器，然后获取图片保存的路径并返回
         */
        //生成一个随机唯一的字符串作为文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String filePath = "E:/code/java/project/demo/target/classes/public/upload/";
        //获取上传的文件名
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        int i = originalFilename.lastIndexOf(".");
        String suffixName = originalFilename.substring(i);

        String newFileName = uuid.concat(suffixName);
        String portraitPath = filePath.concat(newFileName);
        //保存图片
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //响应图片的路径
        String path = "upload/".concat(newFileName);

        return Result.ok(path);
    }

    //从请求头中的token信息获取用户类型，并响应用户信息
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        //验证token是否已经失效
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String, Object> stringObjectLinkedHashMap = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                stringObjectLinkedHashMap.put("userType",1);
                stringObjectLinkedHashMap.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                stringObjectLinkedHashMap.put("userType",2);
                stringObjectLinkedHashMap.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                stringObjectLinkedHashMap.put("userType",3);
                stringObjectLinkedHashMap.put("user",teacher);
                break;
        }
        return Result.ok(stringObjectLinkedHashMap);
    }

    //将前端提交POST请求的信息封装起来，使用@RequestBody注解
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        //校验用户输入的验证码和session中的验证码
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        //1.session中验证码失效了
        if ("".equals(sessionVerifiCode)){
            return Result.fail().message("验证码失效，请刷新页面重试");
        }
        if (!loginVerifiCode.equalsIgnoreCase(sessionVerifiCode)){
            return Result.fail().message("验证码输入有误！");
        }
        //验证码使用完毕，移除当前请求域中的验证码
        session.removeAttribute("verifiCode");

        //准备一个map集合，存放用户响应的信息
        Map<String, Object> map = new LinkedHashMap<>();
        //2.根据选择的用户类型去不同角色的用户表中查询用户，判断用户名和密码是否正确
        switch (loginForm.getUserType()){
            case 1:
                try {
                    //调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Admin对象,找不到返回Null
                    Admin admin = adminService.login(loginForm);
                    if (admin!=null){
                        //登录成功，将用户ID和用户类型转换为token口令，作为信息响应给前端
                        map.put("token",JwtHelper.createToken(admin.getId().longValue(),1));
                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    //捕获异常，向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    //调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Admin对象,找不到返回Null
                    Student student = studentService.login(loginForm);
                    if (student!=null){
                        //登录成功，将用户ID和用户类型转换为token口令，作为信息响应给前端
                        map.put("token",JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    //捕获异常，向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    //调用服务层登录方法,根据用户提交的LoginInfo信息,查询对应的Admin对象,找不到返回Null
                    Teacher teacher = teacherService.login(loginForm);
                    if (teacher!=null){
                        //登录成功，将用户ID和用户类型转换为token口令，作为信息响应给前端
                        map.put("token",JwtHelper.createToken(teacher.getId().longValue(),3));
                    }else {
                        throw new RuntimeException("用户名或者密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    //捕获异常，向用户响应错误信息
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此人!");
    }

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取验证码图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入Session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  POST  http://localhost:8080/sms/system/updatePwd/admin/admin123
    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd,
            @RequestHeader String token
    ){
        //判断token是否失效
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.fail().message("token失效，请重新登录。");
        }

        //从token中获取用户id，用户类型，判断从哪个表中查询
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        //请求链接中原密码和新密码都是以明文方式传输的，数据库中的密码是以密文存储的，所以要将原密码和新密码转换成密文
        String encryptOldPwd = MD5.encrypt(oldPwd);
        String encryptNewPwd = MD5.encrypt(newPwd);

        switch (userType){
            case 1:
                QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
                adminQueryWrapper.eq("id",userId);
                adminQueryWrapper.eq("password",encryptOldPwd);
                Admin one = adminService.getOne(adminQueryWrapper);
                if (one != null){
                    one.setPassword(encryptNewPwd);
                    adminService.saveOrUpdate(one);
                }else {
                    return Result.fail().message("原密码错误！");
                }
                break;
            case 2:
                QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
                studentQueryWrapper.eq("id",userId);
                studentQueryWrapper.eq("password",encryptOldPwd);
                Student studentServiceOne = studentService.getOne(studentQueryWrapper);
                if (studentServiceOne != null){
                    studentServiceOne.setPassword(encryptNewPwd);
                    studentService.saveOrUpdate(studentServiceOne);
                }else {
                    return Result.fail().message("原密码错误!");
                }
                break;
            case 3:
                QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
                teacherQueryWrapper.eq("id",userId);
                teacherQueryWrapper.eq("password",encryptOldPwd);
                Teacher teacherServiceOne = teacherService.getOne(teacherQueryWrapper);
                if (teacherServiceOne != null){
                    teacherServiceOne.setPassword(encryptNewPwd);
                    teacherService.saveOrUpdate(teacherServiceOne);
                }else {
                    return Result.fail().message("原密码错误!");
                }
                break;
        }

        return Result.ok();
    }

}
