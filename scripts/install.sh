#!/usr/bin/env bash
#在本地仓库安装.RELEASE
version=1.2.2
cd ../
cd structure-dependencies
mvn clean install -Dmaven.test.skip=true -Drevision=$version
cd ../structure-common
mvn clean install -Dmaven.test.skip=true -Drevision=$version
cd ../structure-boot-parent
mvn clean install -Dmaven.test.skip=true -Drevision=$version