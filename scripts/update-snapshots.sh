#!/usr/bin/env bash
# 更新快照版本
version=1.2.2-SNAPSHOT
cd ../
cd structure-dependencies
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
cd ../structure-common
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version
cd ../structure-boot-parent
mvn clean deploy -P release,oss -Dmaven.test.skip=true -Drevision=$version