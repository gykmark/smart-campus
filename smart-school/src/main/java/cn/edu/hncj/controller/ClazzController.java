package cn.edu.hncj.controller;

import cn.edu.hncj.pojo.Clazz;
import cn.edu.hncj.service.ClazzService;
import cn.edu.hncj.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("获取全部班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzes = clazzService.getClazzs();
        return Result.ok(clazzes);
    }

    @ApiOperation("新增或修改Grade信息，有id值是修改信息，没有id值是新增信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的Clazz对象")
            @RequestBody Clazz clazz){
        //调用Service层方法完成
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除Clazz信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("要删除的所有的Clazz的id的JSON的集合")
            @RequestBody List<Integer> lists
            ){
        clazzService.removeByIds(lists);
        return Result.ok();
    }

    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数")
            @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询显示的条数")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件")
            Clazz clazz){
        //分页带条件查询
        Page<Clazz> page = new Page<>(pageNo,pageSize);
        //通过服务层
        IPage<Clazz> pageRs = clazzService.getClazzByOpr(page,clazz);
        //封装Result对象返回查询结果
        return Result.ok(pageRs);
    }
}
