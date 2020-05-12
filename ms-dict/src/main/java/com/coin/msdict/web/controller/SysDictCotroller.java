package com.coin.msdict.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.coin.msdict.config.dict.DictLocalCache;
import com.coin.msdict.web.service.SysDictService;
import com.coin.util.BaseException;
import com.coin.msdict.web.vo.SysDictVo;
import com.coin.msdict.web.po.SysDictPo;
import com.coin.msdict.web.dto.SysDictDto;
import com.coin.util.Result;
import com.coin.util.ResultCodeEnum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
* @ClassName SysDictCotroller
* @Description: 字典表
* @Author kh
* @Date 2020-04-30
* @Version V1.0
*/

@Slf4j
@RestController
@RequestMapping("/api/sysdict")
@Api(tags="字典表")
public class SysDictCotroller {

    @Autowired
    private DictLocalCache dictLocalCache;

    @Autowired
    private SysDictService sysDictService;

    @RequestMapping("test")
    public Object test() {
        return dictLocalCache.get();
    }

    /**
    * @MethodName selectSysDictById
    * @Description TODO
    * @param id
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:724.039
    */
    @GetMapping("selectSysDict/{id}")
    @ApiOperation(value="查询字典表")
    public Result selectSysDictById(@PathVariable("id") String id) throws BaseException {
        SysDictVo sysDictVo = sysDictService.selectSysDictById(id);
        return Result.success();
    }

    /**
     * @MethodName selectSysDict
     * @Description TODO
     * @param sysDict
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/30 01:16:725.039
     */
    @PostMapping("selectSysDict")
    @ApiOperation(value="查询字典表")
    public Result selectSysDict(@RequestBody SysDictVo sysDict) throws BaseException {
        sysDictService.selectSysDict(sysDict);
        return Result.success();
    }

    /**
     * @MethodName selectSysDicts
     * @Description TODO
     * @param sysDict
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/30 01:16:725.039
     */
    @PostMapping("selectSysDictsPage")
    @ApiOperation(value="查询字典表")
    public Result selectSysDictsPage(@RequestBody SysDictPo sysDict) throws BaseException {
        IPage<SysDictVo> page = sysDictService.selectSysDictsPage(sysDict);
        return Result.success(page);
    }

    /**
     * @MethodName deleteSysDictById
     * @Description TODO
     * @param id
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/30 01:16:39.725
     */
    @GetMapping("deleteSysDict/{id}")
    @ApiOperation(value="根据主键删除字典表")
    public Result deleteSysDictById(@PathVariable("id") String id) throws BaseException {
        sysDictService.deleteSysDictById(id);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }

    /**
    * @MethodName deleteSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:726.039
    */
    @PostMapping("deleteSysDicts")
    @ApiOperation(value="删除多个字典表")
    public Result deleteSysDicts(@RequestBody List<SysDictVo> sysDictList) throws BaseException {
        sysDictService.deleteSysDicts(sysDictList);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }

    /**
    * @MethodName updateSysDict
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:727.039
    */
    @PostMapping("updateSysDict")
    @ApiOperation(value="更新字典表")
    public Result updateSysDict(@RequestBody SysDictVo sysDict) throws BaseException {
        sysDictService.updateSysDict(sysDict);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    /**
    * @MethodName updateSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:728.039
    */
    @PostMapping("updateSysDicts")
    @ApiOperation(value="更新多个字典表")
    public Result updateSysDicts(@RequestBody List<SysDictVo> sysDictList) throws BaseException {
        sysDictService.updateSysDicts(sysDictList);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    /**
    * @MethodName insertSysDict
    * @Description TODO
    * @param sysDict
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:728.039
    */
    @PostMapping("insertSysDict")
    @ApiOperation(value="插入字典表")
    public Result insertSysDict(@RequestBody SysDictVo sysDict) throws BaseException {
        sysDictService.insertSysDict(sysDict);
        return Result.success(ResultCodeEnum.SAVE_SUCCESS);
    }

    /**
    * @MethodName insertSysDicts
    * @Description TODO
    * @param sysDictList
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/30 01:16:729.039
    */
    @PostMapping("insertSysDicts")
    @ApiOperation(value="插入多个字典表")
    public Result insertSysDicts(@RequestBody List<SysDictVo> sysDictList) throws BaseException  {
        sysDictService.insertSysDicts(sysDictList);
        return Result.success(ResultCodeEnum.SAVE_SUCCESS);
    }

    /**
     * @MethodName importSysDicts
     * @Description 导入数据
     * @param sysDictVos
     * @return com.coin.util.Result
     * @throws
     * @author kh
     * @date 2020/5/12 12:40
     */
    @PostMapping("importSysDicts")
    @ApiOperation(value="导入数据")
    public Result importSysDicts(@RequestBody List<SysDictVo> sysDictVos) throws BaseException {
        return Result.success();
    }
}
