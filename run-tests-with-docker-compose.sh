#!/bin/bash

source restart-docker.sh
source push-tests.sh

test_profile=${1:-all}
TIMESTAMP=$(date +"%Y%m%d_%H%M")
TEST_OUTPUT_DIR=./test-output/$TIMESTAMP

mkdir -p "$TEST_OUTPUT_DIR/logs"
mkdir -p "$TEST_OUTPUT_DIR/results"
mkdir -p "$TEST_OUTPUT_DIR/report"

echo ">>> Скачивание образа ${DOCKER_IMAGE}"
docker pull ${DOCKER_IMAGE}

echo ">>> Запущены ${test_profile} тесты"
docker run --rm \
  --network nbank-network \
  -v "$TEST_OUTPUT_DIR/logs":/app/logs \
  -v "$TEST_OUTPUT_DIR/results":/app/target/surefire-reports \
  -v "$TEST_OUTPUT_DIR/report":/app/target/site \
  -e test_profile="${test_profile}" \
  -e db_username=postgres \
  -e db_password=postgres \
${DOCKER_IMAGE}

echo ">>> Тесты завершены"
echo "Лог файл: $TEST_OUTPUT_DIR/logs/run.log"
echo "Результаты тестов: $TEST_OUTPUT_DIR/results"
echo "Репорт: $TEST_OUTPUT_DIR/report"