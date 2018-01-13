package com.cas.sim.tis.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;

import tk.mybatis.mapper.entity.Condition;

public interface BaseService<T>extends Remote {
	int getTotal() throws RemoteException;

	int getTotalBy(Condition condition) throws RemoteException;

	void save(T model) throws RemoteException;// 持久化

	void save(List<T> models) throws RemoteException;// 批量持久化

	void deleteById(Integer id) throws RemoteException;// 通过主鍵刪除

	void deleteByIds(String ids) throws RemoteException;// 批量刪除 eg：ids -> “1,2,3,4”

	void update(T model) throws RemoteException;// 更新

	T findById(Integer id) throws RemoteException;// 通过ID查找

	T findBy(String fieldName, Object value) throws RemoteException, TooManyResultsException; // 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

	List<T> findByIds(String ids)throws RemoteException;// 通过多个ID查找//eg：ids -> “1,2,3,4”

	List<T> findByCondition(Condition condition)throws RemoteException;// 根据条件查找

	List<T> findAll()throws RemoteException;// 获取所有
}
