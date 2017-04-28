# biwan-framework

脚手架生成程序

运行步骤

先编译一下：

1. 到根目录
   mvn clean  install
2. 到Test目录执行下面语句
   mvn archetype:generate -DarchetypeGroupId=com.itar.soa -DarchetypeArtifactId=biwan-framework -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=product -DartifactId=product-biz -
e


这样就会在Test里面生成一个叫做product-biz的项目
