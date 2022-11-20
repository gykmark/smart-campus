package cn.edu.hncj.service;

import cn.edu.hncj.pojo.Admin;
import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * AdminService中的login方法
 */
public interface AdminService extends IService<Admin>{
    /**
     * 登录
     * @param loginForm
     * @return
     */
    Admin login(LoginForm loginForm);

    /**
     *通过id查询管理员信息
     */
    Admin getAdminById(Long userId);

    IPage<Admin> getAdminByOpr(Page<Admin> page, Admin admin);
}
