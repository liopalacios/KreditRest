package com.servicekerdit.service;

import com.servicekerdit.entity.ClienteKredit;

public interface ClienteService {

    ClienteKredit findByClienteidp(String clienteid);
}
