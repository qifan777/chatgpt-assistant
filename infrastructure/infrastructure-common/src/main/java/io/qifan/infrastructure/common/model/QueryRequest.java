package io.qifan.infrastructure.common.model;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Data
public class QueryRequest<T> implements Request {
    // 动态的查询条件
    private T query;
    // 分页大小
    private Integer pageSize;
    // 页数
    private Integer pageNum;
    // 排序字段
    private Map<String, String> sorts;

    /**
     * 将QueryRequest对象转成pageable对象
     *
     * @return 分页对象
     */
    public Pageable toPageable() {
        return PageRequest.of(getPageNum() - 1,
                              getPageSize(),
                              Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * 将QueryRequest对象转成pageable对象，支持自定义排序
     *
     * @param orders 动态的查询条件
     * @return 分页对象
     */

    public Pageable toPageable(Sort.Order... orders) {
        return PageRequest.of(getPageNum() - 1,
                              getPageSize(),
                              Sort.by(orders));
    }
}
