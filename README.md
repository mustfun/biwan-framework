# biwan-framework

脚手架生成程序

运行步骤

先编译一下：

1. 到根目录  
   mvn clean  install
2. 到Test目录执行下面语句  
   mvn archetype:generate -DarchetypeGroupId=com.itar.soa -DarchetypeArtifactId=biwan-framework -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.itar.soa -DartifactId=product -e


这样就会在Test里面生成一个叫做product-biz的项目


参考：http://maven.apache.org/archetype/archetype-models/archetype-descriptor/archetype-descriptor.html  

参考：https://maven.apache.org/guides/mini/guide-creating-archetypes.html
