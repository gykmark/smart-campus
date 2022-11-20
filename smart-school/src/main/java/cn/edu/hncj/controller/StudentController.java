package cn.edu.hncj.controller;

import cn.edu.hncj.pojo.Clazz;
import cn.edu.hncj.pojo.Student;
import cn.edu.hncj.pojo.Teacher;
import cn.edu.hncj.service.StudentService;
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

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("添加或者修改学生信息，有id是修改，无id是添加")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("JSON格式的学生对象")
            @RequestBody Student student){
        //获取学生id
        Integer id = student.getId();
        if(id == 0 || id == null){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        //调用Service层方法完成
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    @ApiOperation("删除学生信息")
    @DeleteMapping("/deleteStudent")
    public Result deleteTeacher(
            @ApiParam("删除学生信息id的JSON格式")
            @RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数")
            @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询显示的条数")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Student student){
        //分页带条件查询
        Page<Student> page = new Page<>(pageNo,pageSize);
        //通过服务层
        IPage<Student> pageRs = studentService.getClazzByOpr(page,student);
        //封装Result对象返回查询结果
        return Result.ok(pageRs);
    }
}
