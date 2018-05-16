package com.cas.sim.tis.services.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.entity.Examination;
import com.cas.sim.tis.services.ExaminationService;

@Service()
public class ExaminationServiceImpl extends AbstractService<Examination> implements ExaminationService {}
