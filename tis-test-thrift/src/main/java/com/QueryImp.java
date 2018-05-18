package com;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TException;

import com.alibaba.fastjson.JSONObject;
import com.thrift.QryResult;
import com.thrift.TestQry;

public class QueryImp implements TestQry.Iface {
	@Override
	public QryResult qryTest(int qryCode) throws TException {
		QryResult result = new QryResult();

		List<User> users = new ArrayList<User>();
		for(int i=0;i<1000;i++) {
			User user = new User();
			user.setName("xiaoming");
			user.setAge(18);
			users.add(user);
		}
		String msg = JSONObject.toJSONString(users);
		System.out.println(msg);
		if (qryCode == 1) {
			result.code = 1;
			result.msg = msg;
		} else {
			result.code = 0;
			result.msg = "fail";
		}
		return result;
	}
}