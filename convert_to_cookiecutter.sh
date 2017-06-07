#!/bin/bash
echo "replacing com.xjeffrose.xrpc with {{cookiecutter.java_package}}"
sed -e 's/com.xjeffrose.xrpc/{{cookiecutter.java_package}}/g' -i "" $(git grep -c com.xjeffrose.xrpc | awk -F: '{print $1}')

echo "replacing xrpc_pid with {{cookiecutter.app_name.replace('-', '_')}}_pid"
sed -e "s/xrpc_pid/{{cookiecutter.app_name.replace('-', '_')}}_pid/g" -i "" bin/startServer.sh

echo "replacing xrpc with {{cookiecutter.app_name}}"
sed -e 's/xrpc/{{cookiecutter.app_name}}/g' -i "" $(git grep -c xrpc | awk -F: '{print $1}')

echo "moving src/main/java/com/xjeffrose/xrpc to src/main/java/{{cookiecutter.java_package_dir}}"
git mv src/main/java/com/xjeffrose/xrpc 'src/main/java/{{cookiecutter.java_package_dir}}'

echo "moving src/test/java/com/xjeffrose/xrpc to src/test/java/{{cookiecutter.java_package_dir}}"
git mv src/test/java/com/xjeffrose/xrpc 'src/test/java/{{cookiecutter.java_package_dir}}'

echo "reseating project root under {{cookiecutter.app_name}}"
mkdir '{{cookiecutter.app_name}}'
git mv .editorconfig .gitignore Dockerfile JavaStandards.md README.md application.conf bin/ pom.xml src/ '{{cookiecutter.app_name}}'
