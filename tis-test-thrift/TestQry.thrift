/**
* 文件名为TestQry.thrift
* 实现功能：创建一个查询结果struct和一个服务接口service
* 基于：thrift-0.9.2
**/
namespace java com.cas.sim.tis.thrift
struct ThriftEntity {
        /**
        *返回码, 1成功，0失败
        */
        1:i32 code;
        /**
        *响应信息
        */
        2:string msg;
        /**
        * 返回数据
        */
        3:string data;
}

namespace java com.cas.sim.tis.service
service UserService{
        ThriftEntity login(1:string usercode, 2:string password),

        ThriftEntity findTeachers(),

        ThriftEntity findTeachers(1:i32 pageIndex, 2:i32 pageSize),

        ThriftEntity findStudents(1:i32 pageIndex, 2:i32 pageSize, 3:i32 classId)
}

service ClassService{
        ThriftEntity findClasses(1:i32 pageIndex, 2:i32 pageSize)，

        ThriftEntity findClassesByTeacher(1:i32 teacherId)，

        ThriftEntity saveClasses(1:string infos, 2:i32 creator)，

        ThriftEntity modifyClass(1:string info)
}