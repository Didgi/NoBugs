#!/bin/bash

set -e
source Docker.env

echo ">>> Авторизация в docker"
echo "${DOCKER_TOKEN}" | docker login -u ${DOCKER_USERNAME} --password-stdin

while true; do
    read -p "Введите значение тэга для создания образа: " TAG;

    IMAGE_NAME=nbank-tests
    DOCKER_IMAGE="${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG}"

    echo ">>> Проверка существования образа в dockerhub"

    if docker manifest inspect "${DOCKER_IMAGE}" >/dev/null 2>&1; then
        echo "❌ Образ с таким тэгом уже существует"
    else
        break
    fi
done

#DOCKER_IMAGE=${DOCKER_USERNAME}/${IMAGE_NAME}:${TAG};

echo ">>> Старт сборки докер образа"
docker build -t ${DOCKER_IMAGE} .

echo ">>> Собран докер образ с именем: ${DOCKER_IMAGE}"
echo ">>> Старт публикации образа в dockerhub"

docker push ${DOCKER_IMAGE};

echo ">>> Образ с именем: ${DOCKER_IMAGE} опубликован в dockerhub"
echo ">>> Скачать данный образ можно выполнив команду: docker pull ${DOCKER_IMAGE}"