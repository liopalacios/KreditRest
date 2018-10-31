package com.servicekerdit.service.impl;

import com.servicekerdit.entity.ClienteKredit;
import com.servicekerdit.repository.ClienteRepository;
import com.servicekerdit.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public ClienteKredit findByClienteidp(String clienteid) {
        ClienteKredit clienteKredit = clienteRepository.findByClienteid(clienteid);
        System.out.print(clienteKredit);
        return clienteKredit;
    }
}
