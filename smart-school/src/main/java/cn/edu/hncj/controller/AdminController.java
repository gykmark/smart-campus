package cn.edu.hncj.controller;

import cn.edu.hncj.pojo.Admin;
import cn.edu.hncj.pojo.Teacher;
import cn.edu.hncj.service.AdminService;
import cn.edu.hncj.util.MD5;
import cn.edu.hncj.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("添加或者修改管理员信息，有id是修改，无id是添加")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateTeacher(
            @ApiParam("JSON格式的管理员对象")
            @RequestBody Admin admin){
        //获取管理员id
        Integer id = admin.getId();
        if(id == 0 || id == null){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        //调用Service层方法完成
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }


    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteTeacher(
            @ApiParam("删除管理员信息id的JSON格式")
            @RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询的页码树")
            @PathVariable("pageNo") Integer pageNO,
            @ApiParam("分页查询页的大小")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的条件")
                    Admin admin){
        Page<Admin> page = new Page<>(pageNO, pageSize);
        IPage<Admin> adminIPage = adminService.getAdminByOpr(page,admin);
        return Result.ok(adminIPage);
    }
}
