package com.mtuci.vkr.repository;
import com.mtuci.vkr.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory,Long> {

    List<PriceHistory> findByProductId(Long productId);
}
