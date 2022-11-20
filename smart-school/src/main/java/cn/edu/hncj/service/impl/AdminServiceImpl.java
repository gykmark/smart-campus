package cn.edu.hncj.service.impl;

import cn.edu.hncj.mapper.AdminMapper;
import cn.edu.hncj.pojo.Admin;
import cn.edu.hncj.pojo.Clazz;
import cn.edu.hncj.pojo.LoginForm;
import cn.edu.hncj.pojo.Teacher;
import cn.edu.hncj.service.AdminService;
import cn.edu.hncj.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {

    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));

        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);

        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> page, Admin admin) {
        //设置查询条件
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        //判断管理员名字是否为空
        String adminName = admin.getName();
        if (!StringUtils.isEmpty(adminName)){
            queryWrapper.like("name",adminName);
        }

        //设置排序规则
        queryWrapper.orderByDesc("id");
        //分页查询数据
        Page<Admin> clazzPage = baseMapper.selectPage(page, queryWrapper);
        return clazzPage;
    }
}
