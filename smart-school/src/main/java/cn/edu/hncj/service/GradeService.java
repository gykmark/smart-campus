package cn.edu.hncj.service;

import cn.edu.hncj.pojo.Grade;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface GradeService extends IService<Grade> {
    List<Grade> getGrades();

    IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName);
}
