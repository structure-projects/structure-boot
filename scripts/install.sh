#!/usr/bin/env bash
#在本地仓库安装

mvn clean install org.apache.maven.plugins:maven-deploy-plugin:2.8:deploy -DskipTests -s ..\..\setting.xml