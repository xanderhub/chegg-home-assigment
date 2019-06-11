package com.chegg.assignment.repositories;

import com.chegg.assignment.domain.DataSource;
import com.chegg.assignment.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
    Page<Question> findAllBySource(final DataSource dataSource, final Pageable pageable);
}
