package cn.edu.hncj.service.impl;

import cn.edu.hncj.mapper.TeacherMapper;
import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Teacher;
import cn.edu.hncj.service.TeacherService;
import cn.edu.hncj.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper,Teacher> implements TeacherService {

    /**
     * Teacher登录方法
     * @param loginForm
     * @return
     */
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);

        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        //判断学生信息是否为空
        String clazz_name = teacher.getClazzName();
        if (!StringUtils.isEmpty(clazz_name)) {
            queryWrapper.like("clazz_name",clazz_name);
        }
        //判断教师名字是否为空
        String name = teacher.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name",name);
        }
        //设置教师排序
        queryWrapper.orderByDesc("id");
       return baseMapper.selectPage(page, queryWrapper);
    }
}
