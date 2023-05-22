package com.codein.repository.article.viewlog;

import com.codein.domain.article.ViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewLogRepository extends JpaRepository<ViewLog, Long>,ViewLogRepositoryCustom {

}
