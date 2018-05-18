CREATE DATABASE `jzg-tool-edit` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `contrast_material_library` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL COMMENT '数据报表名称',
  `TYPE_ID` int(10) NOT NULL COMMENT '对标材料类型ID',
  `TYPE_NAME` varchar(45) DEFAULT NULL COMMENT '对标材料类型名称\n(脏数据,为了提高查询效率)',
  `CONTENT` text COMMENT '对标材料内容',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对标材料库';

CREATE TABLE `contrast_material_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL COMMENT '对标材料库类型名称',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='对标材料库类型';

CREATE TABLE `contrast_stardard_library` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_ID` varchar(45) DEFAULT NULL COMMENT '标准库类型ID',
  `TYPE_NAME` varchar(45) DEFAULT NULL COMMENT '标准库类型名称\n（脏字段，为了提高查询效率）',
  `MAJOR_ID` varchar(45) DEFAULT NULL COMMENT '专业ID',
  `MAJOR_NAME` varchar(45) DEFAULT NULL COMMENT '专业名称\n（脏字段，为了提高查询效率）',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准库';

CREATE TABLE `contrast_stardard_library_course_design` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LIBRARY_ID` int(11) NOT NULL COMMENT '标准库记录ID',
  `GOAL` text COMMENT '培养目标',
  `CONTENT` text COMMENT '课程内容',
  `CHAPTER` text COMMENT '章节管理',
  `SCHEDULE` text COMMENT '教学进度',
  `FIRST_COURSE_DESIGN` text COMMENT '第一堂课设计',
  `METHOD_EXAM` text COMMENT '考核方法',
  `REFERENCE` text COMMENT '教材与参考资料',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准库-人才培养方案国家标准';

CREATE TABLE `contrast_stardard_library_national_course` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LIBRARY_ID` int(11) NOT NULL COMMENT '所属标准库ID',
  `BASIC` text COMMENT '基本信息',
  `ORIENTATION` text COMMENT '职业面向',
  `GOAL` text COMMENT '培养目标',
  `COURSE` text COMMENT '课程结构及教学进程安排',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准库-人才培养方案国家标准';

CREATE TABLE `contrast_stardard_library_national_tranning_plan` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LIBRARY_ID` int(11) NOT NULL COMMENT '所属标准库ID',
  `BASIC` text COMMENT '基本信息',
  `ORIENTATION` text COMMENT '职业面向',
  `GOAL` text COMMENT '培养目标',
  `COURSE` text COMMENT '课程结构及教学进程安排',
  `GUARANTEE` text COMMENT '实施保障',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准库-人才培养方案国家标准';

CREATE TABLE `contrast_stardard_library_province_tranning_plan` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LIBRARY_ID` int(11) NOT NULL COMMENT '所属标准库ID',
  `BASIC` text COMMENT '基本信息',
  `ORIENTATION` text COMMENT '职业面向',
  `GOAL` text COMMENT '培养目标',
  `COURSE` text COMMENT '课程结构及教学进程安排',
  `GUARANTEE` text COMMENT '实施保障',
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准库-人才培养方案国家标准';

CREATE TABLE `contrast_stardard_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL COMMENT '类型名称\n1、人才培养方案国家标准\n2、人才培养方案省级标准\n3、课程标准设计国家标准\n4、课程整体教学设计国家标准',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对照标准-类型表。';

CREATE TABLE `course_stardard_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  `NAME` varchar(45) DEFAULT NULL COMMENT '课程名称',
  `PERIOD` varchar(45) DEFAULT NULL COMMENT '参考学时',
  `CREDIT` varchar(45) DEFAULT NULL COMMENT '课程学分',
  `TASK` text COMMENT '课程性质与任务',
  `GOAL` text COMMENT '课程教学目标',
  `ANALYSIS_STUDY` text COMMENT '学情分析',
  `METHOD_TECH` text COMMENT '教学方法',
  `METHOD_EVALUTE` text COMMENT '评价方法',
  `CONDITION_TECH` text COMMENT '教学条件',
  `RESOURCE_DEV` text COMMENT '资源开发',
  `BOOK_WRITTEN` text COMMENT '教材编写',
  `EXTRA` text COMMENT '其它',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程标准:基本信息';

CREATE TABLE `course_stardard_requirement` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  `TEACH_ITEM` varchar(45) DEFAULT NULL COMMENT '教学项目',
  `REQUIREMENT` varchar(500) DEFAULT NULL COMMENT '课程内容与教学要求',
  `ACTIVE_DESIGN` varchar(500) DEFAULT NULL COMMENT '活动设计/技能实训点',
  `PERIOD` varchar(45) DEFAULT NULL COMMENT '参考课时',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程标准:教学内容与要求';

CREATE TABLE `draw_up_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL COMMENT '编制工具数据方案名称',
  `TYPE_ID` int(11) DEFAULT NULL COMMENT '材料类型ID',
  `TYPE_NAME` varchar(45) DEFAULT NULL COMMENT '材料类型名称',
  `MAJOR_ID` int(11) DEFAULT NULL,
  `MAJOR_NAME` varchar(45) DEFAULT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `USER_ID` varchar(36) DEFAULT NULL COMMENT '使用UUID表示用户ID。\n格式：8-4-4-4-12，加上“-”符号，共36位字符。',
  `DEL` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8 COMMENT='编制工具数据列表';

CREATE TABLE `draw_up_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL COMMENT '编制工具的名称,等同于材料类型名称\n1、人才培养方案\n2、专业教学计划\n3、课程标准',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='编制工具类型；\n每一种工具对应编辑一种材料。';

CREATE TABLE `teach_design_content` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL,
  `CONTENT` varchar(45) DEFAULT NULL,
  `PERIOD` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教学整体设计:课程内容';

CREATE TABLE `teach_design_first_design` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL,
  `CONTENT` text COMMENT '步骤内容',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教学整体设计:第一堂课设计';

CREATE TABLE `teach_design_goal` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL,
  `GOAL` varchar(45) DEFAULT NULL COMMENT '教学目标',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教学整体设计:课程目标';

CREATE TABLE `teach_design_goal_ask` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GOAL_ID` int(11) NOT NULL,
  `TYPE` tinyint(4) DEFAULT NULL COMMENT 'ASK类型:\n0: A\n1: S\n2: K',
  `ASK_DESC` varchar(45) DEFAULT NULL COMMENT 'ASK的描述',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `teach_design_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL,
  `ORIENTATE` text COMMENT '课程定位',
  `METHOD_EXAM` text COMMENT '考核方法',
  `REFERENCE` varchar(45) DEFAULT NULL COMMENT '教材与参考资料',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教学整体设计:课程定位';

CREATE TABLE `teach_design_schedule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL,
  `WEEK` tinyint(4) DEFAULT NULL COMMENT '周次',
  `PERIOD` tinyint(4) DEFAULT NULL COMMENT '学时',
  `SUBJECT` varchar(45) DEFAULT NULL COMMENT '项目主题',
  `GOAL` varchar(45) DEFAULT NULL COMMENT '课程目标',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教学整体设计:教学进度';

CREATE TABLE `training_plan_career_orientation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENT` text COMMENT '自定义内容',
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编制-人才培养方案:职业面向表-未对接';

CREATE TABLE `training_plan_career_orientation_docking` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CAREER_ID` int(11) DEFAULT NULL COMMENT '岗位ID\n对接其他系统的岗位ID',
  `CAREER_NAME` varchar(45) DEFAULT NULL COMMENT '岗位名称\n对接其他系统的岗位名称\n（脏字段，为了提高效率）',
  `TRANNING_GOAL` text COMMENT '培训目标',
  `CAREER_CERTIFICATION` varchar(45) DEFAULT NULL COMMENT '所需证书',
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编制-人才培养方案:职业面向表-对接平台数据';

CREATE TABLE `training_plan_course` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  `NAME` varchar(45) DEFAULT NULL COMMENT '课程名称',
  `TYPE` varchar(45) DEFAULT NULL COMMENT '课程类型',
  `APPROACH` varchar(45) DEFAULT NULL COMMENT '授课方式',
  `PERIOD` tinyint(4) DEFAULT NULL COMMENT '学时',
  `CREDIT` tinyint(4) DEFAULT NULL COMMENT '学分',
  `THEORY_DURATION` varchar(45) DEFAULT NULL COMMENT '理论教学时长',
  `PRACTICE_DURATION` varchar(45) DEFAULT NULL COMMENT '实践教学时长',
  `TERM_1` tinyint(4) DEFAULT NULL COMMENT '第一学期',
  `TERM_2` tinyint(4) DEFAULT NULL COMMENT '第二学期',
  `TERM_3` tinyint(4) DEFAULT NULL COMMENT '第三学期',
  `TERM_4` tinyint(4) DEFAULT NULL COMMENT '第四学期',
  `TERM_5` tinyint(4) DEFAULT NULL COMMENT '第五学期',
  `TERM_6` tinyint(4) DEFAULT NULL COMMENT '第六学期',
  `EXAM` varchar(6) DEFAULT NULL COMMENT '考试\n例如：123，表示在第1、2、3学期有考试',
  `CHECK` varchar(6) DEFAULT NULL COMMENT '考核\n例如：123，表示在第1、2、3学期有考核',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编制-人才培养方案:课程结构及教学进程安排';

CREATE TABLE `training_plan_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DRAW_UP_ID` int(11) NOT NULL COMMENT '编制工具数据ID',
  `MAJOR_NAME` varchar(45) DEFAULT NULL COMMENT '专业名称及代码',
  `ENTRY_REQUIREMENT` varchar(45) DEFAULT NULL COMMENT '入学要求',
  `LIMIT` varchar(45) DEFAULT NULL COMMENT '修业年限',
  `GRADUATE_REQUIREMENT` varchar(500) DEFAULT NULL COMMENT '毕业要求',
  `GOAL` text COMMENT '培养目标',
  `GUARANTEE_PLAN` text COMMENT '保障计划',
  `TEACHER_PLAN_1` varchar(500) DEFAULT NULL COMMENT '师资队伍\n当前',
  `TEACHER_PLAN_2` varchar(500) DEFAULT NULL COMMENT '师资队伍\n第二年',
  `TEACHER_PLAN_3` varchar(500) DEFAULT NULL COMMENT '师资队伍\n第三年',
  `EQUIPMENT_PLAN_1` varchar(500) DEFAULT NULL COMMENT '教学设备\n当前',
  `EQUIPMENT_PLAN_2` varchar(500) DEFAULT NULL COMMENT '教学设备\n第二年',
  `EQUIPMENT_PLAN_3` varchar(500) DEFAULT NULL COMMENT '教学设备\n第三年',
  `EXTRA` text COMMENT '其它',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编制-人才培养方案:基本信息表';
