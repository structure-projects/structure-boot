#!/usr/bin/env bash
#发行新的版本上传到中心仓库可以执行这个脚本
#!/bin/bash
version=1.2.2
cd ../
cd structure-dependencies
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
cd ../structure-common
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
cd ../structure-boot-parent
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version