package com.coin.msdict.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coin.msdict.config.dict.DictLocalCache;
import com.coin.msdict.web.entity.SysDict;
import com.coin.msdict.web.mapper.SysDictMapper;
import com.coin.msdict.web.service.SysDictService;
import com.coin.util.BaseException;
import com.coin.util.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coin.util.ResultCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coin.msdict.web.po.SysDictPo;
import com.coin.msdict.web.vo.SysDictVo;

import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
* @ClassName SysDictServiceImpl
* @Description: TODO
* @Author kh
* @Date 2020-04-29
* @Version V1.0
*/
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    DictLocalCache dictLocalCache;

    /**
    * @MethodName selectSysDictById
    * @Description TODO
    * @param id
    * @return
    * @throws BaseException
    * @author
    * @date 2020/04/29 05:18:645.058
    */
    @Override
    public SysDictVo selectSysDictById(Serializable id) throws BaseException {
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysDict::getId, id);
        SysDict sysDict = sysDictMapper.selectOne(queryWrapper);
        SysDictVo sysDictVo = new SysDictVo();
        if(null != sysDict) {
            BeanUtil.copyProperties(sysDictVo, sysDict);
        }
        return sysDictVo;
    }

    /**
     * @MethodName selectSysDict
     * @Description TODO
     * @param sysDict
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:648.058
     */
    @Override
    public SysDictVo selectSysDict(SysDictVo sysDict) throws BaseException {
        return sysDictMapper.selectOneSelective(sysDict);
    }

    /**
     * @MethodName selectSysDictsPage
     * @Description TODO
     * @param sysDict
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:648.058
     */
    @Override
    public IPage<SysDictVo> selectSysDictsPage(SysDictPo sysDict) throws BaseException {
        Page page = new Page(sysDict.getCurrent(), sysDict.getSize());
        return page.setRecords(sysDictMapper.selectSysDictsPage(page, sysDict));
    }

    @Override
    public List<SysDict> selectSysDicts(SysDict sysDict) throws BaseException {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(SysDict::getCode, sysDict.getCode())
                .and(c -> c.eq(SysDict::getIsUse, sysDict.getIsUse()));
        List<SysDict> list = list(wrapper);
        return list;
    }

    /**
     * @MethodName deleteSysDictById
     * @Description TODO
     * @param id
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:58.648
     */
    @Override
    public int deleteSysDictById(Serializable id) throws BaseException {
        // 需要去删除缓存中的数据
        SysDictVo sysDictVo = selectSysDictById(id);

        sysDictMapper.deleteById(id);
        if(!Objects.isNull(sysDictVo)) {
            SysDict sysDict = new SysDict();
            BeanUtil.copyProperties(sysDict, sysDictVo);
            dictLocalCache.remove(sysDict);
        }
        return 1;
    }

    /**
     * @MethodName deleteSysDicts
     * @Description TODO
     * @param sysDictList
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:648.058
     */
    @Override
    public int deleteSysDicts(List<SysDictVo> sysDictList) throws BaseException {
        List<String> ids = new ArrayList<>();
        List<SysDict> sds = new ArrayList<>();
        for(SysDictVo sd : sysDictList) {
            ids.add(sd.getId());
            SysDict sysDict = new SysDict();
            BeanUtil.copyProperties(sysDict, sd);
            sds.add(sysDict);
        }
        sysDictMapper.deleteBatchIds(ids);

        dictLocalCache.remove(sds);
        return sysDictList.size();
    }

    /**
     * @MethodName updateSysDict
     * @Description 存在漏洞，如果修改了type_code或者code，需要传回来，将缓存中的删掉
     * @param sysDict
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:648.058
     */
    @Override
    public int updateSysDict(SysDictVo sysDict) throws BaseException {
        if(StringUtils.isNotEmpty(sysDict.getCode())) {
            sysDict.setCode(sysDict.getCode().toUpperCase());
        }
        sysDict.setCreateTime(null);
        sysDict.setUpdateTime(LocalDateTime.now());
        sysDictMapper.updateByPrimaryKeySelective(sysDict);
        SysDict sysDict1 = new SysDict();
        BeanUtil.copyProperties(sysDict1, sysDict);
        dictLocalCache.update(sysDict1);
        return 1;
    }

    /**
     * @MethodName updateSysDicts
     * @Description TODO
     * @param sysDictList
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:649.058
     */
    @Override
    public int updateSysDicts(List<SysDictVo> sysDictList) throws BaseException {
        for(SysDictVo dv : sysDictList) {
            this.updateSysDict(dv);
        }
        return sysDictList.size();
    };

    /**
     * @MethodName insertSysDict
     * @Description TODO
     * @param sysDictVo
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:649.058
     */
    @Override
    public int insertSysDict(SysDictVo sysDictVo) throws BaseException {
        if(StringUtils.isEmpty(sysDictVo.getCode())) {
            throw new BaseException(ResultCodeEnum.CODE_NULL);
        }
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(sysDict, sysDictVo);
        sysDict.setCode(sysDictVo.getCode().toUpperCase());
        sysDict.setCreateTime(LocalDateTime.now());
        sysDict.setUpdateTime(sysDict.getCreateTime());
        sysDictMapper.insert(sysDict);
        if(sysDict.getIsUse() == 1) {
            dictLocalCache.add(sysDict);
        } else {
            dictLocalCache.remove(sysDict);
        }
        return 1;
    }

    /**
     * @MethodName insertSysDicts
     * @Description TODO
     * @param sysDictList
     * @return
     * @throws BaseException
     * @author
     * @date 2020/04/29 05:18:649.058
     */
    @Override
    public int insertSysDicts(List<SysDictVo> sysDictList) throws BaseException {
        for(SysDictVo sysDict : sysDictList) {
            insertSysDict(sysDict);
        }
        return sysDictList.size();
    }

    /**
     * @MethodName insertImport
     * @Description 导出数据
     * @param lists
     * @return int
     * @throws
     * @author kh
     * @date 2020/5/13 15:16
     */
    @Override
    public int insertImport(List<List<String>> lists) throws BaseException {
        List<SysDict> dicts = new ArrayList<SysDict>();
        LocalDateTime now = LocalDateTime.now();
        for(List<String> list : lists) {
            SysDict sysDict = new SysDict();
            sysDict.setId(UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
            sysDict.setCode(list.get(0));
            sysDict.setName(list.get(1));
            sysDict.setFullPinyin(list.get(2));
            sysDict.setSimplePinyin(list.get(3));
            sysDict.setUpCode(list.get(4));
            sysDict.setDescp(list.get(5));
            sysDict.setTypeCode(list.get(6));
            sysDict.setTypeName(list.get(7));
            if(StringUtils.isNotEmpty(list.get(8))) {
                double v = Double.parseDouble(list.get(8));
                sysDict.setSeq(new Double(v).intValue());
            }
            sysDict.setSqlCode(list.get(9));
            if(StringUtils.isNotEmpty(list.get(10))) {
                double w = Double.parseDouble(list.get(10));
                sysDict.setIsUse(new Double(w).intValue());
            }
            sysDict.setCreateTime(now);
            sysDict.setUpdateTime(now);
            dicts.add(sysDict);
        }
        sysDictMapper.insertBatch(dicts);
        dictLocalCache.add(dicts);
        return dicts.size();
    }
}
