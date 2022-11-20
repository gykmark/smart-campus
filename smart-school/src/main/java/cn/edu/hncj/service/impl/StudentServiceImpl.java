package cn.edu.hncj.service.impl;

import cn.edu.hncj.mapper.StudentMapper;
import cn.edu.hncj.pojo.Admin;
import cn.edu.hncj.pojo.Clazz;
import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Student;
import cn.edu.hncj.service.StudentService;
import cn.edu.hncj.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService {

    /**
     * 学生登录方法
     * @return
     */
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);

        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public IPage<Student> getClazzByOpr(Page<Student> page, Student student) {
        //设置查询条件
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        //判断班级名字是否为空
        String studentName = student.getName();
        if (!StringUtils.isEmpty(studentName)){
            queryWrapper.like("name",studentName);
        }

        //判断年级名字是否为空
        String clazz_name = student.getClazzName();
        if (!StringUtils.isEmpty(clazz_name)){
            queryWrapper.like("clazz_name",clazz_name);
        }

        //设置排序规则
        queryWrapper.orderByDesc("id");
        //分页查询数据
        Page<Student> studentPage = baseMapper.selectPage(page, queryWrapper);
        return studentPage;
    }
}
