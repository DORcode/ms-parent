package com.coin.msdict.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coin.msdict.web.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coin.util.BaseException;
import com.coin.util.Result;
import com.coin.msdict.web.po.SysDictPo;
import com.coin.msdict.web.vo.SysDictVo;
import com.coin.msdict.web.dto.SysDictDto;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SysDictService
 * @Description: TODO
 * @Author kh
 * @Date 2020-04-29
 * @Version V1.0
 */
public interface SysDictService extends IService<SysDict> {

    /**
    * @MethodName selectSysDictById
    * @Description TODO
    * @param id
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:626.058
    */
    SysDictVo selectSysDictById(Serializable id) throws BaseException;

   /**
    * @MethodName selectSysDict
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
    SysDictVo selectSysDict(SysDictVo sysDict) throws BaseException;

   /**
    * @MethodName selectSysDictsPage
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
    IPage<SysDictVo> selectSysDictsPage(SysDictPo sysDict) throws BaseException;

   /**
   * @MethodName deleteSysDict
   * @Description TODO
   * @param id
   * @return
   * @throws BaseException
   * @author
   * @date 2020/04/29 05:18:58.627
   */
   int deleteSysDictById(Serializable id) throws BaseException;

   /**
    * @MethodName deleteSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
   int deleteSysDicts(List<SysDictVo> sysDictList) throws BaseException;

   /**
    * @MethodName updateSysDict
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
   int updateSysDict(SysDictVo sysDict) throws BaseException;

   /**
    * @MethodName updateSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
   int updateSysDicts(List<SysDictVo> sysDictList) throws BaseException;

   /**
    * @MethodName insertSysDict
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
   int insertSysDict(SysDictVo sysDict) throws BaseException;

   /**
    * @MethodName insertSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:627.058
    */
   int insertSysDicts(List<SysDictVo> sysDictList) throws BaseException;

   /**
    * @MethodName insertImport
    * @Description 保存导入
    * @param lists
    * @return int
    * @throws
    * @author kh
    * @date 2020/5/13 12:04
    */
   int insertImport(List<List<String>> lists) throws BaseException;

 }
