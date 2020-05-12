package com.coin.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageInfo
 * @Description: TODO
 * @Author kh
 * @Date 2020/1/16 17:29
 * @Version V1.0
 **/
@Data
public class PageInfo implements Serializable {

    /**
     * 总数量
     */
    private long total;
    /**
     * 当前页数
     */
    @JSONField(name="pageNum")
    private long current;
    /**
     * 每页数量
     */
    @JSONField(name="pageSize")
    private int size;

    /**
     * 总页数
     */
    @JSONField(name="totalPage")
    private long pages;
    /**
     * 数据
     */
    private List<?> data;

    public PageInfo(long total, List<?> data) {
        this.total = total;
        this.data = data;
    }

    public PageInfo(long total, long current, int size, List<?> data) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.data = data;
        this.pages = this.calPages();
    }

    private long calPages() {
        if(total % size == 0) {
            return total / size;
        } else {
            return total / size + 1;
        }
    }
}
