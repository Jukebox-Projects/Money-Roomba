package com.moneyroomba.repository;

import com.moneyroomba.domain.Category;
import com.moneyroomba.domain.UserDetails;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    public List<Category> findAllByUser(UserDetails user);
}
