package cn.edu.hncj.controller;

import cn.edu.hncj.pojo.Teacher;
import cn.edu.hncj.service.TeacherService;
import cn.edu.hncj.util.MD5;
import cn.edu.hncj.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("添加或者修改教师信息，有id是修改，无id是添加")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("JSON格式的教师对象")
            @RequestBody Teacher teacher){
        //获取教师id
        Integer id = teacher.getId();
        if(id == 0 || id == null){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        //调用Service层方法完成
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    @ApiOperation("删除教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("删除教师信息id的JSON格式")
            @RequestBody List<Integer> ids){
        teacherService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("分页查询的页码书")
            @PathVariable("pageNo") Integer pageNO,
            @ApiParam("分页查询页的大小")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的条件")
            Teacher teacher){
        Page<Teacher> page = new Page<>(pageNO, pageSize);
        IPage<Teacher> teacherIPage = teacherService.getTeacherByOpr(page,teacher);
        return Result.ok(teacherIPage);
    }
}
