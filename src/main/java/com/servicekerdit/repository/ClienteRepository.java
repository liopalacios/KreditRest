package com.servicekerdit.repository;

import com.servicekerdit.entity.ClienteKredit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteKredit,String> {

    ClienteKredit findByClienteidp(String clienteid);
}
