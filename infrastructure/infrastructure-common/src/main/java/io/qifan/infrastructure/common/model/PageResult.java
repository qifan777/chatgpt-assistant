package io.qifan.infrastructure.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class PageResult<T> {
    private Integer total;
    private Integer totalPages;
    private Integer pageSize;
    private Integer pageNum;
    private List<T> list;

    public PageResult(List<T> list, Long total, Integer pageSize, Integer pageNum) {
        this.list = list;
        this.total = total.intValue();
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public static <T> PageResult<T> of(List<T> list, Long total, Integer pageSize, Integer pageNumber) {
        return new PageResult<>(list, total, pageSize, pageNumber);
    }


}
