DROP TABLE if exists `elec_comp`;

CREATE TABLE `elec_comp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(45) NOT NULL COMMENT '类型名称,如:继电器(类)',
  `NAME` varchar(45) NOT NULL COMMENT '元器件名称',
  `MODEL` varchar(45) NOT NULL COMMENT '元器件型号，如：cj-100',
  `MDL_PATH` varchar(45) NOT NULL COMMENT '模型路径',
  `CFG_PATH` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MODEL_UNIQUE` (`MODEL`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='元器件类别表';

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'接触器',
'交流接触器',
'CJX2-1210',
'Model/CJX2-1210/CJX2-1210.j3o',
'configurations/Accontactor/CJX2-1210.xml'
);

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'接触器',
'接触器辅助触点',
'F4-22',
'Model/F4-22/F4-22.j3o',
'configurations/Accontactor/F4-22.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'断路器',
'低压断路器（1P）',
'NBE7-C25-1P',
'Model/NBE7-C25-1P/NBE7-C25-1P.j3o',
'configurations/Breaker/NBE7-C25-1P.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'底座',
'熔断器座',
'RT28N-32X',
'Model/RT28N-32X/RT28N-32X.j3o',
'configurations/Fuse/RT28N-32X.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'底座',
'时间继电器底座（8孔）',
'CZS08X',
'Model/CZS08X-E/CZS08X-E.j3o',
'configurations/Relay/CZS08X.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'底座',
'继电器底座（8孔）',
'CZY08A',
'Model/CZY08A/CZY08A.j3o',
'configurations/Relay/CZY08A.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'底座',
'继电器底座（14孔）',
'CZY14A',
'Model/CZY14A/CZY14A.j3o',
'configurations/Relay/CZY14A.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'继电器',
'热继电器',
'JR36-20',
'Model/JR36-20/JR36-20.j3o',
'configurations/Relay/JR36-20.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'继电器',
'中间继电器',
'JZ7-44',
'Model/JZ7-44/JZ7-44.j3o',
'configurations/Relay/JZ7-44.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'继电器',
'交流220V继电器',
'JZX-22F(D)-2Z(AC)',
'Model/JZX-22F(D)-2Z(AC)/JZX-22F(D)-2Z(AC).j3o',
'configurations/Relay/JZX-22F(D)-2Z(AC).xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'继电器',
'直流24V继电器',
'JZX-22F(D)-2Z(DC)',
'Model/JZX-22F(D)-2Z(DC)/JZX-22F(D)-2Z(DC).j3o',
'configurations/Relay/JZX-22F(D)-2Z(DC).xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'继电器',
'直流24V继电器',
'NR2-25',
'Model/NR2-25/NR2-25.j3o',
'configurations/Relay/NR2-25.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'开关',
'按钮盒',
'NP2-E3001',
'Model/NP2-E3001/NP2-E3001.j3o',
'configurations/Switch/NP2-E3001.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'开关',
'按钮',
'NP4-11BN',
'Model/NP4-11BN/NP4-11BN.j3o',
'configurations/Switch/NP4-11BN.xml');

INSERT INTO `db_cas_electrical`.`elec_comp`
(`TYPE`,`NAME`,`MODEL`,`MDL_PATH`,`CFG_PATH`)VALUES(
'开关',
'行程开关',
'YBLX-K1-111',
'Model/YBLX-K1/YBLX-K1.j3o',
'configurations/Switch/YBLX-K1-111.xml');

