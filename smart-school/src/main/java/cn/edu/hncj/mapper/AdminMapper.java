package cn.edu.hncj.mapper;

import cn.edu.hncj.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
