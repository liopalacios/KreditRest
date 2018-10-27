package com.servicekerdit.service.impl;

import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.repository.ClienteRepository;
import com.servicekerdit.service.ClienteService;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {
    ClienteRepository clienteRepository;

    @Override
    public ClienteKredit findByClienteidp(String clienteid) {
        return clienteRepository.findByClienteidp(clienteid.trim());
    }
}
