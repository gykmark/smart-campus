package cn.edu.hncj.service;

import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    /**
     * 学生登录方法
     * @return
     */
    Student login(LoginForm loginForm);

    /**
     *通过id查询学生信息
     */
    Student getStudentById(Long userId);

    IPage<Student> getClazzByOpr(Page<Student> page, Student student);
}
