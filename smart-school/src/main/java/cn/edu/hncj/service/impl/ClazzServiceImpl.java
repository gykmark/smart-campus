package cn.edu.hncj.service.impl;

import cn.edu.hncj.mapper.ClazzMapper;
import cn.edu.hncj.pojo.Clazz;
import cn.edu.hncj.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {
        //设置查询条件
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();

        //判断班级名字是否为空
        String clazzName = clazz.getName();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("name",clazzName);
        }

        //判断年级名字是否为空
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }

        //设置排序规则
        queryWrapper.orderByDesc("id");
        //分页查询数据
        Page<Clazz> clazzPage = baseMapper.selectPage(page, queryWrapper);
        return clazzPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}
