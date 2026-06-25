package cn.structure.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * <p>
 * 仓储接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/23 16:09
 */
public interface IRepository<T, ID> extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {


}
