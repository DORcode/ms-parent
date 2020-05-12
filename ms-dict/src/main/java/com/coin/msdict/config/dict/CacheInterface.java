package com.coin.msdict.config.dict;

import com.coin.msdict.web.entity.SysDict;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName CacheInterface
 * @Description: TODO
 * @Author kh
 * @Date 2020/4/30 15:05
 * @Version V1.0
 **/
public interface CacheInterface {

    void load();

    void reload();

    void add(SysDict sysDict);

    void update(SysDict sysDict);

    void remove(SysDict sysDict);

    void clear();

    Set<SysDict> getList(String key);

    SysDict get(String key);

    Map get();

    Set<SysDict> getDictItems(String key);

    SysDict getDictItem(String key);
}
