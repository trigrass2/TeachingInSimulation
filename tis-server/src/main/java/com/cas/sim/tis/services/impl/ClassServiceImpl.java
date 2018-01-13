package com.cas.sim.tis.services.impl;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;

@Service("classService")
public class ClassServiceImpl extends AbstractService<Class> implements ClassService {}
