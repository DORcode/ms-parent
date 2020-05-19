package com.coin.msdict.config.dict;

import com.coin.msdict.web.entity.SysDict;
import com.coin.msdict.web.service.SysDictService;
import com.coin.msdict.web.vo.SysDictVo;
import com.coin.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @ClassName DictCache
 * @Description: 字典本地缓存
 * @Author kh
 * @Date 2020/5/4 9:48
 * @Version V1.0
 **/
@Slf4j
@Component
public class DictLocalCache implements CacheInterface {
    // 根据编码来存储
    public static final ConcurrentMap<String, Set<SysDict>> CACHELISTMAP = new ConcurrentHashMap<String, Set<SysDict>>();
    // sql;key
    public static final Set<SysDict> SQLS = new HashSet<>();

    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private DictDataSourceConfig dictDataSourceConfig;

    @PostConstruct
    void init() {
        this.reload();
    }

    @Override
    public void load() {
        // 从数据库中查询，需要去除未启用的
        List<SysDict> list = sysDictService.list();

        // 遍历，处理无sql的字典和有sql的字典数据存储到内存中
        for (SysDict sd : list) {
            add(sd);
        }
    }

    @Override
    public void reload() {
        clear();
        load();
    }
    
    /**
     * @MethodName add
     * @Description 新增字典
     * @param sd 
     * @return void
     * @throws 
     * @author kh
     * @date 2020/5/11 21:07
     */
    @Override
    public void add(SysDict sd) {
        rwl.writeLock().lock();
        try {
            // 字典数据
            if (StringUtils.isEmpty(sd.getSqlCode()) && sd.getIsUse() == 1) {
                add(sd.getCode() + "_" + sd.getTypeCode(), sd);
                addSet(sd.getCode(), sd);
            } else if (StringUtils.isNotEmpty(sd.getSqlCode()) && sd.getIsUse() == 1) {
                String sqlCode = sd.getSqlCode();
                if (StringUtils.isNotEmpty(sqlCode)) {
                    String[] split = sqlCode.split(";");
                    String sql = split[0];
                    String key = split[1];
                    List<SysDict> query = queryByJdbc(key, sql);
                    addList(query);
                    SQLS.add(sd);
                }
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    @Override
    public void add(List<SysDict> sysDicts) {
        rwl.writeLock().lock();
        try {
            for (SysDict sd : sysDicts) {
                add(sd);
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    private void add(String key, SysDict sd) {
        try {
            Set<SysDict> set = getList(key);
            if(CollectionUtils.isEmpty(set)) {
                Set<SysDict> sysDicts = new HashSet<>();
                sysDicts.add(sd);
                CACHELISTMAP.put(key, sysDicts);
            } else {
                set.clear();
                set.add(sd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSet(String code, SysDict sd) {
        try {
            Set<SysDict> set = getList(code);
            if(CollectionUtils.isEmpty(set)) {
                Set<SysDict> sysDicts = new HashSet<>();
                sysDicts.add(sd);
                CACHELISTMAP.put(code, sysDicts);
            } else {
                set.add(sd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addList(List<SysDict> list) {
        for(SysDict sd : list) {
            add(sd.getCode() + "_" + sd.getTypeCode(), sd);
            addSet(sd.getCode(), sd);
        }
    }

    /**
     * @MethodName update
     * @Description 更新字典后，区分出是带sql不带sql，是否是不启用，需要未更新前的对象
     * @param sysDict
     * @return void
     * @throws
     * @author kh
     * @date 2020/5/11 21:10
     */
    @Override
    public void update(SysDict sysDict) {
        rwl.writeLock().lock();
        try {
            // 更新时，如果为字典数据
            if (StringUtils.isEmpty(sysDict.getSqlCode())) {
                // 当是启用，
                if (sysDict.getIsUse() == 1) {
                    add(sysDict.getCode() + "_" + sysDict.getTypeCode(), sysDict);
                    // set可能存在修改不成功的可能
                    Set<SysDict> set = getList(sysDict.getCode());
                    if (CollectionUtils.isEmpty(set)) {
                        addSet(sysDict.getCode(), sysDict);
                    } else {
                        Iterator<SysDict> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            SysDict next = iterator.next();
                            if (StringUtils.isNotEmpty(next.getId()) && next.getId().equals(sysDict.getId()) || next.getCode().equals(sysDict.getCode()) && next.getTypeCode().equals(sysDict.getTypeCode())) {
                                iterator.remove();
                            }
                        }
                        set.add(sysDict);
                    }
                } else {
                    // 未启用时
                    remove(sysDict);
                }
            } else {
                if (sysDict.getIsUse() == 1) {
                    // 获得数据源， 查询出来数据
                    String sqlCode = sysDict.getSqlCode();
                    String[] split = sqlCode.split(";");
                    String sql = split[0];
                    String key = split[1];
                    List<SysDict> query = queryByJdbc(key, sql);
                    if (query.size() > 0) {
                        remove(sysDict);
                        addList(query);
                    }

                    // 先删除
                    Iterator<SysDict> iterator = SQLS.iterator();
                    while (iterator.hasNext()) {
                        SysDict next = iterator.next();
                        if (StringUtils.isNotEmpty(next.getId()) && next.getId().equals(sysDict.getId()) || next.getCode().equals(sysDict.getCode()) & next.getTypeCode().equals(sysDict.getTypeCode())) {
                            iterator.remove();
                        }
                    }
                    SQLS.add(sysDict);
                } else {
                    remove(sysDict);
                }
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    @Override
    public void remove(SysDict sysDict) {
        rwl.writeLock().lock();
        try {
            // 删除list缓存中的数据
            Set<SysDict> sysDicts = CACHELISTMAP.get(sysDict.getCode());
            if (CollectionUtils.isNotEmpty(sysDicts)) {
                Iterator<SysDict> iterator = sysDicts.iterator();
                while (iterator.hasNext()) {
                    SysDict next = iterator.next();
                    if (StringUtils.isNotEmpty(next.getId()) && next.getId().equals(sysDict.getId()) || next.getCode().equals(sysDict.getCode()) && next.getTypeCode().equals(sysDict.getTypeCode()) || next.getIsUse() == 0) {
                        iterator.remove();
                    }
                }
            }

            // 删除item缓存中的数据
            CACHELISTMAP.remove(sysDict.getCode() + "_" + sysDict.getTypeCode());

            if (StringUtils.isNotEmpty(sysDict.getSqlCode())) {
                Iterator<SysDict> iterator = SQLS.iterator();
                while (iterator.hasNext()) {
                    SysDict next = iterator.next();
                    if (next.getCode().equals(sysDict.getCode()) & next.getTypeCode().equals(sysDict.getTypeCode())) {
                        iterator.remove();
                    }
                }
            }
        } finally {
            rwl.writeLock().unlock();
        }
    }

    @Override
    public void remove(List<SysDict> sysDicts) {
        for(SysDict sd : sysDicts) {
            remove(sd);
        }
    }

    @Override
    public void clear() {
        CACHELISTMAP.clear();
        SQLS.clear();
    }

    // @Override
    private Set<SysDict> getList(String key) {
        return CACHELISTMAP.get(key);

    }

    private SysDict get(String key) {
        Iterator<SysDict> iterator = getList(key).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return null;
        }
    }

    @Override
    public Map get() {
        return CACHELISTMAP;
    }

    private Set<SysDict> gets(String key) {
        Set<SysDict> sysDicts = new HashSet<>();
        return CACHELISTMAP.getOrDefault(key, sysDicts);
    }

    @Override
    public Set<SysDict> getDictItems(String key) {
        rwl.readLock().lock();

        try {
            return gets(key);

            // 如果没有需要处理，从数据库中查询，目前假设，如果是sql字典，根据code查询只有一条，如果是非sql字典，至少一条，不存在sql和字典并存的情况
        } finally {
            rwl.readLock().unlock();
        }
    }

    @Override
    public SysDict getDictItem(String code, String type) {
        rwl.readLock().lock();
        try {
            Iterator<SysDict> iterator = gets(code + "_" + type).iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            } else {

//                SysDictVo sysDict = new SysDictVo();
//                // 这样拆不合适，code中有下划线
//                sysDict.setCode(key.split("_")[0]);
//                sysDict.setTypeCode(key.split("_")[1]);
//                sysDict.setIsUse(1);
//                SysDictVo sysDictVo = sysDictService.selectSysDict(sysDict);
//                if(!Objects.isNull(sysDict)) {
//                    SysDict sysDict1 = new SysDict();
//                    BeanUtil.copyProperties(sysDict1, sysDictVo);
//                    rwl.readLock().unlock();
//                    add(sysDict1);
//                    return sysDict1;
//                }
                return new SysDict();
            }
        } finally {
            rwl.readLock().unlock();
        }
    }

    private List<SysDict> queryByJdbc(String key, String sql) {
        List<SysDict> list = new ArrayList<>();
        try {
            if(StringUtils.isNotEmpty(key)) {
                // 查询出它库数据昨为字典
                 list = dictDataSourceConfig
                    .getJdbcTemplate(key)
                    .query(sql, new BeanPropertyRowMapper<>(SysDict.class));
            }
        } catch (Exception e) {
        }
        return list;
    }
}
