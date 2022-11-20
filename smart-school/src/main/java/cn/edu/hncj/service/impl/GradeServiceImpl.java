package cn.edu.hncj.service.impl;

import cn.edu.hncj.mapper.GradeMapper;
import cn.edu.hncj.pojo.Grade;
import cn.edu.hncj.service.GradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);
    }

    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> pageParam, String gradeName) {
        // 设置查询条件
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("name",gradeName);
        }
        // 设置排序规则
        queryWrapper.orderByDesc("id");
        //queryWrapper.orderByAsc("name");
        // 分页查询数据
        Page page = baseMapper.selectPage(pageParam, queryWrapper);

        return page;
    }
}
