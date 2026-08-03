#!/bin/bash

set -e
source ./Docker.env

ENV=COMMIT_HASH
echo ">>> Получен хэш коммита: ${COMMIT_HASH}"

echo ">>> Авторизация в docker"
echo "${DOCKER_TOKEN}" | docker login -u ${DOCKER_USERNAME} --password-stdin

IMAGE_NAME=nbank-tests
SHORT_COMMIT_HASH=${COMMIT_HASH::7}
DOCKER_IMAGE="${DOCKER_USERNAME}/${IMAGE_NAME}:${SHORT_COMMIT_HASH}"

echo ">>> Старт сборки докер образа"
docker build -t ${DOCKER_IMAGE} .

echo ">>> Собран докер образ с именем: ${DOCKER_IMAGE}"
echo ">>> Старт публикации образа в dockerhub"

docker push ${DOCKER_IMAGE};

echo ">>> Образ с именем: ${DOCKER_IMAGE} опубликован в dockerhub"
echo ">>> Скачать данный образ можно выполнив команду: docker pull ${DOCKER_IMAGE}"