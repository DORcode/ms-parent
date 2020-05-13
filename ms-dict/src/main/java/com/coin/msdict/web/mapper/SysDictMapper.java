package com.coin.msdict.web.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coin.msdict.web.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.coin.msdict.web.po.SysDictPo;
import com.coin.msdict.web.vo.SysDictVo;
import com.coin.msdict.web.dto.SysDictDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @ClassName SysDict
* @Description: SysDictMapper
* @Author kh
* @Date 2020-04-30
* @Version V1.0
*/
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
    * @MethodName selectOneSelective
    * @Description TODO
    * @param sysDict
    * @return
    * @throws
    * @author
    * @date 2020/04/30 02:41:552.043
    */
    SysDictVo selectOneSelective(SysDictVo sysDict);

    /**
    * @MethodName selectSysDictsPage
    * @Description TODO
    * @param page
    * @param sysDict
    * @return
    * @throws
    * @author
    * @date 2020/04/30 02:41:562.043
    */
    List<SysDictVo> selectSysDictsPage(Page page, @Param("sysDict") SysDictPo sysDict);

    /**
    * @MethodName insertSelective
    * @Description TODO
    * @param sysDict
    * @return
    * @throws
    * @author
    * @date 2020/04/30 02:41:562.043
    */
    int insertSelective(SysDict sysDict);

    int insertBatch(List<SysDict> sysDicts);

    /**
    * @MethodName updateByPrimaryKeySelective
    * @Description TODO
    * @param sysDict
    * @return
    * @throws
    * @author
    * @date 2020/04/30 02:41:562.043
    */
    int updateByPrimaryKeySelective(SysDictVo sysDict);
}
