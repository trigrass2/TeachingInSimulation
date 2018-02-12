package com.cas.sim.tis.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.ElecCompMapper;
import com.cas.sim.tis.mapper.IMapper;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.util.SpringUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DBTest {

	@Resource
	private UserMapper userMapper;

	@Resource
	private ElecCompMapper elecCompMapper;

	@Test
	public void testElecComp() throws Exception {
		elecCompMapper.selectAll().forEach(System.out::println);
	}
	@Test
	public void testUserMapper() throws Exception {
		userMapper.selectAll().forEach(System.out::println);
	}

	@Test
	public void testAdminLogin() throws Exception {
		Condition condition = new Condition(User.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("code", "admin");
		criteria.andEqualTo("password", "123456");
		List<User> users = userMapper.selectByCondition(condition);
		Assert.assertEquals(1, users.size());
	}

	@Test
	public void testShow() {
		File file = new File("src/main/java/com/cas/sim/tis/mapper");
		Arrays.asList(file.listFiles()).stream().filter(f -> !"IMapper.java".equals(f.getName())).forEach(f -> {
			try {
				String name = f.getName().substring(0, f.getName().lastIndexOf(".java"));
				System.out.println("DBTest.testShow()" + name);
				IMapper mapper = (IMapper) SpringUtil.getBean(StringUtils.uncapitalize(name), Class.forName("com.cas.sim.tis.mapper." + name));
				mapper.selectAll();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
}
