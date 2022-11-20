package cn.edu.hncj.service;

import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TeacherService extends IService<Teacher> {
    /**
     * 登录方法
     */
    Teacher login(LoginForm loginForm);

    /**
     *通过id查询老师信息
     */
    Teacher getTeacherById(Long userId);

    /**
     * 分页带条件查询老师信息
     * @param page
     * @param teacher
     * @return
     */
    IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher);
}
