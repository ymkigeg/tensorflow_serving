
mount_dir=$(pwd)/models/
docker run -t -p 8001:8001 -p 8002:8002 \
    -v "${mount_dir}:/models/" root/tensorflow-serving:latest \
    --model_config_file=/models/models.config \
    --model_config_file_poll_wait_seconds=60 \
    --allow_version_labels_for_unavailable_models=true \
    --port=8001 --rest_api_port=8002 \
    --enable_batching=true