package br.com.f1rst.datas.user.repository;

import br.com.f1rst.datas.user.entity.LogAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAddressRepository extends JpaRepository<LogAddressEntity, Long> {



}
