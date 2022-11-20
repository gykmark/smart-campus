package cn.edu.hncj.controller;

import cn.edu.hncj.pojo.Grade;
import cn.edu.hncj.service.GradeService;
import cn.edu.hncj.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 查询年级信息
 */
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("获取全部年级信息")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有的Grade的id的JSON的集合")@RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok();
    }

    @ApiOperation("新增或修改Grade信息，有id值是修改信息，没有id值是新增信息")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON格式的Grade对象")
            @RequestBody Grade grade){
        //调用参数
        //调用服务层方法完成增减或修改
        boolean b = gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @ApiOperation("根据年级名称模糊查询Grade信息，带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数")
            @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配")
            String gradeName){
        //分页，带条件查询
        Page<Grade> page = new Page<>(pageNo,pageSize);
        //通过服务层
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page,gradeName);
        //封装Result对象并返回
        return Result.ok(pageRs);
    }
}
