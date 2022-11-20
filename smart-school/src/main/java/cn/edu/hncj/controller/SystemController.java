package cn.edu.hncj.controller;


import cn.edu.hncj.pojo.*;
import cn.edu.hncj.service.AdminService;
import cn.edu.hncj.service.GradeService;
import cn.edu.hncj.service.StudentService;
import cn.edu.hncj.service.TeacherService;
import cn.edu.hncj.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("修改账户密码处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("响应的token")
            @RequestHeader("token") String token,
            @ApiParam("旧的密码")
            @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新的密码")
            @PathVariable("newPwd") String newPwd){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().message("登录失效，请重新登录后修改密码");
        }
        //获取用户的ID和使用类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        //解密旧密码和新密码
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        //根据不同的用户使用不同的方法
        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper0 = new QueryWrapper<>();
                queryWrapper0.eq("id",userId.intValue());
                queryWrapper0.eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper0);
                if(admin != null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("旧密码错误");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper1);
                if(student != null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("旧密码错误");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper2);
                if(teacher != null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("旧密码错误");
                }
                break;
        }
        return Result.ok();
    }

    @ApiOperation("上传人物图片")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("图片文件")
            @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request){
        //生成随机的uid，防止图片名称重复
        String uuid = UUID.randomUUID().toString().replace("_", "").toLowerCase();
        String filename = multipartFile.getOriginalFilename();
        int lastIndexOf = filename.lastIndexOf(".");
        String newFileName = uuid + filename.substring(lastIndexOf);
        //保存文件
        String portraitPath="D:/IDEA_example/Smart-School/smart-school/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //响应图片路径
        String path = "/upload/".concat(newFileName);
        return Result.ok(path);
    }

    @ApiOperation("判断用户类型")
    @GetMapping("/getInfo")
    public Result getInfoByToken(
            @ApiParam("生成的token")
            @RequestHeader("token") String token){
        //判断token是否有效,ture异常，false有效
        boolean expiration = JwtHelper.isExpiration(token);
        if(expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出 用户id 和 用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        //使用map存储数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("登录页面")
    @PostMapping("/login")
    public Result login(
            @ApiParam("登录用户信息")
            @RequestBody LoginForm loginForm,HttpServletRequest request){

        //校验码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(sessionVerifiCode) || sessionVerifiCode == null){
            return Result.fail().message("输入验证码为空，请刷新后再试");
        }
        if(!loginVerifiCode.equals(sessionVerifiCode)){
            return Result.fail().message("验证码输入有误，请刷新后再试");
        }
        //从session中移除验证码
        session.removeAttribute(sessionVerifiCode);

        //准备一个map用户存放响应的数据
        Map<String,Object> map = new LinkedHashMap<>();
        //分用户类型进行校验
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (admin != null) {
                        //用户的类型和用户id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(),1));
                    }else {
                        throw  new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (student != null) {
                        //用户的类型和用户id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(student.getId().longValue(),2));
                    }else {
                        throw  new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (teacher != null) {
                        //用户的类型和用户id转换成一个密文，以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(),3));
                    }else {
                        throw  new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }

        return Result.fail().message("用户不存在");
    }

    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取图片
        BufferedImage codeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码放入session域中，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器
        try {
            ImageIO.write(codeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
