package com.mtuci.vkr.repository;

import com.mtuci.vkr.model.MainInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<MainInfo,Long> {
}
